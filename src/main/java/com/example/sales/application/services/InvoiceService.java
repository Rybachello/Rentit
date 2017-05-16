package com.example.sales.application.services;

import com.example.common.application.exceptions.InvoiceNotFoundException;
import com.example.inventory.infrastructure.IdentifierFactory;
import com.example.sales.application.dto.InvoiceDTO;
import com.example.sales.application.dto.RemittanceAdviceDTO;
import com.example.sales.application.gateways.InvoicingGateway;
import com.example.sales.domain.model.Invoice;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.InvoiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by stepan on 16/05/2017.
 */
@Service
public class InvoiceService {
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    InvoiceAssembler invoiceAssembler;
    @Autowired
    InvoicingGateway invoicingGateway;
    @Autowired
    @Qualifier("objectMapper")
    ObjectMapper mapper;

    @Value("${gmail.username}")
    String gmailUsername;

    public Invoice createInvoiceForPurchaseOrder(PurchaseOrder po) {
        Invoice invoice = new Invoice(IdentifierFactory.nextID(), false, po.getIssueDate().plusMonths(1), null, po.getTotal(), po);
        invoiceRepository.save(invoice);

        return invoice;
    }

    public void sendInvoice(Invoice invoice) {
        JavaMailSender mailSender = new JavaMailSenderImpl();
        InvoiceDTO invoiceDTO = invoiceAssembler.toResource(invoice);

        String invoiceJSON = null;
        try {
            invoiceJSON = mapper.writeValueAsString(invoiceDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        MimeMessage rootMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {
            helper = new MimeMessageHelper(rootMessage, true);
            helper.setFrom(gmailUsername + "@gmail.com");
            helper.setTo(invoice.getPurchaseOrder().getCustomer().getEmail());
            helper.setSubject("Invoice for purchase order " + invoice.getPurchaseOrder().getId());
            helper.setText("Dear customer, \n\n Please find attached the invoice corresponding to your purchase order. \n\n Kindly yours, \n\n RentIt team!");
            helper.addAttachment("invoice.json", new ByteArrayDataSource(invoiceJSON, "application/json"));
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        invoicingGateway.sendInvoice(rootMessage);
    }

    public void acceptRemittance(RemittanceAdviceDTO remittance) throws InvoiceNotFoundException {
        Invoice invoice = invoiceRepository.findOne(remittance.getInvoiceId());

        if (invoice == null) {
            throw new InvoiceNotFoundException("Invoice not found");
        }

        invoice.setPaid(true);
        invoice.setPaidDate(LocalDate.now());

        invoiceRepository.flush();
    }

    @Scheduled(cron="0 0 4 * * FRI") // Every Friday, 4AM
    public void sendUnpaidInvoiceReminders() {
        List<Invoice> invoices = invoiceRepository.findByPaid(false);

        for(Invoice i : invoices) {
            this.sendInvoice(i);
        }
    }
}
