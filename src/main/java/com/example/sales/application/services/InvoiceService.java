package com.example.sales.application.services;

import com.example.inventory.infrastructure.IdentifierFactory;
import com.example.sales.application.dto.InvoiceDTO;
import com.example.sales.application.dto.RemittanceDTO;
import com.example.sales.domain.model.Invoice;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.model.Remittance;
import com.example.sales.domain.repository.InvoiceRepository;
import com.example.sales.integration.InvoicingGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

/**
 * Created by Rybachello on 5/3/2017.
 */
@Service
public class InvoiceService {
    @Autowired
    @Qualifier("objectMapper")
    ObjectMapper mapper;

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    InvoicingGateway invoicingGateway;

    @Value("${gmail.username}")
    String gmailUsername;

    @Value("${gmail.password}")
    String gmailPassword;

    public Invoice createInvoice(PurchaseOrder purchaseOrder) {
        Invoice invoice = Invoice.of(IdentifierFactory.nextID(), purchaseOrder);
        invoiceRepository.save(invoice);

        return invoice;
    }

    //todo: need to implement
    public void sendInvoice(Invoice invoice, String email) throws Exception {

        JavaMailSender mailSender = new JavaMailSenderImpl();
        String json = mapper.writeValueAsString(
                InvoiceDTO.of(invoice.getId(), invoice.getPurchaseOrder().getId(), invoice.getPurchaseOrder().getTotal(), invoice.getPurchaseOrder().getPaymentSchedule()));

        MimeMessage rootMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(rootMessage, true);

        helper.setFrom(gmailUsername + "@gmail.com");
        helper.setTo(email);
        helper.setSubject("Invoice Purchase Order" + invoice.getPurchaseOrder().getId());
        helper.setText("Dear customer,\n\nPlease find attached the Invoice corresponding to your Purchase Order.\n\nKindly yours,\n\nRentIt Team!");
        helper.addAttachment("invoice.json", new ByteArrayDataSource(json, "application/json"));


        invoicingGateway.sendInvoice(rootMessage);
    }

    public Invoice closeInvoice(RemittanceDTO remittanceDTO) {
        Invoice invoice = invoiceRepository.findOne(remittanceDTO.getInvoiceId());

        invoice.closeInvoice(Remittance.of(remittanceDTO.getId(), remittanceDTO.getNote()));

        invoiceRepository.save(invoice);
        return invoice;
    }
}
