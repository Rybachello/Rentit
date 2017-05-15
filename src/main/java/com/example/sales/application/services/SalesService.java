package com.example.sales.application.services;

import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.common.application.exceptions.InvoiceNotFoundException;
import com.example.common.application.exceptions.PlantNotAvailableException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.application.services.BusinessPeriodDisassembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.domain.validation.BusinessPeriodValidator;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.inventory.infrastructure.IdentifierFactory;
import com.example.sales.application.dto.InvoiceDTO;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.Customer;
import com.example.sales.application.gateways.InvoicingGateway;
import com.example.sales.domain.model.Invoice;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.InvoiceRepository;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import com.example.sales.domain.validation.PurchaseOrderValidator;
import com.example.sales.rest.controller.RemittanceRestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Rybachello on 3/7/2017.
 */
@Service
public class SalesService {
    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;
    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    BusinessPeriodDisassembler businessPeriodDisassembler;
    @Autowired
    InvoicingGateway invoicingGateway;
    @Autowired
    InvoiceAssembler invoiceAssembler;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    @Qualifier("objectMapper")
    ObjectMapper mapper;

    @Value("${gmail.username}")
    String gmailUsername;

    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO dto, Customer customer) throws PlantNotAvailableException, InvalidPurchaseOrderStatusException {

        //find first purchase order
        PlantInventoryEntry plantInventoryEntry = plantInventoryEntryRepository.findOne(dto.getPlant().get_id());
        //convert to dto
        BusinessPeriod businessPeriod = businessPeriodDisassembler.toResources(dto.getRentalPeriod());
        //create the purchase order with PENDING STATUS
        PurchaseOrder po = PurchaseOrder.of(IdentifierFactory.nextID(), LocalDate.now(), businessPeriod,null, plantInventoryEntry,dto.getConstructionSite(), customer);


        try {
            inventoryService.createPlantReservation(dto.getPlant().get_id(), businessPeriod, po);
            po.confirmReservation(plantInventoryEntry.getPrice());
            //save to the database
            DataBinder binder = new DataBinder(po);
            binder.addValidators(new PurchaseOrderValidator(new BusinessPeriodValidator()));
            binder.validate();

            if (!binder.getBindingResult().hasErrors()) {
                purchaseOrderRepository.save(po);
            }
        } catch (PlantNotAvailableException e) {
            po.rejectPurchaseOrder();
            DataBinder binder = new DataBinder(po);
            binder.addValidators(new PurchaseOrderValidator(new BusinessPeriodValidator()));
            binder.validate();
            if (!binder.getBindingResult().hasErrors())
                purchaseOrderRepository.save(po);

            throw e;
        }

        PurchaseOrderDTO poDto = purchaseOrderAssembler.toResource(po);
        return poDto;
    }

    public PurchaseOrderDTO updatePurchaseOrder(PurchaseOrderDTO dto, Customer customer) throws PlantNotAvailableException, InvalidPurchaseOrderStatusException {
        BusinessPeriod businessPeriod = businessPeriodDisassembler.toResources(dto.getRentalPeriod());
        PurchaseOrder po = purchaseOrderRepository.findByIdAndCustomer(dto.get_id(), customer);
        po = inventoryService.updatePlantReservation(po, businessPeriod);
        purchaseOrderRepository.flush();
        PurchaseOrderDTO poDto = purchaseOrderAssembler.toResource(po);
        return poDto;
    }


    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        return purchaseOrderAssembler.toResources(purchaseOrders);
    }

    public PurchaseOrderDTO getPurchaseOrderById(String id, Customer customer) throws PurchaseOrderNotFoundException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByIdAndCustomer(id,customer);
        if (purchaseOrder == null) {
            throw new PurchaseOrderNotFoundException("Purchase order not found");
        }

        return purchaseOrderAssembler.toResource(purchaseOrder);
    }

    public PurchaseOrderDTO getPurchaseOrderById(String id) throws PurchaseOrderNotFoundException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(id);
        if (purchaseOrder == null) {
            throw new PurchaseOrderNotFoundException("Purchase order not found");
        }

        return purchaseOrderAssembler.toResource(purchaseOrder);
    }

    public PurchaseOrderDTO acceptPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(purchaseOrderDTO.get_id());

        if (purchaseOrder == null) {
            throw new PurchaseOrderNotFoundException("Purchase order not found");
        }

        purchaseOrder.acceptPurchaseOrder();

        purchaseOrderRepository.save(purchaseOrder);

        PurchaseOrderDTO updatedDTO = purchaseOrderAssembler.toResource(purchaseOrder);

        return updatedDTO;
    }

    public PurchaseOrderDTO rejectPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(purchaseOrderDTO.get_id());

        if (purchaseOrder == null) {
            throw new PurchaseOrderNotFoundException("Purchase order not found");
        }

        purchaseOrder.rejectPurchaseOrder();

        purchaseOrderRepository.save(purchaseOrder);

        PurchaseOrderDTO updatedDTO = purchaseOrderAssembler.toResource(purchaseOrder);

        return updatedDTO;
    }

    public PurchaseOrderDTO rejectPOByCustomer(PurchaseOrderDTO purchaseOrderDTO) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(purchaseOrderDTO.get_id());

        if (purchaseOrder == null) {
            throw new PurchaseOrderNotFoundException("Purchase order not found");
        }

        purchaseOrder.rejectByCustomer();

        purchaseOrderRepository.save(purchaseOrder);

        PurchaseOrderDTO updatedDTO = purchaseOrderAssembler.toResource(purchaseOrder);

        return updatedDTO;
    }

    public PurchaseOrderDTO closePurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(purchaseOrderDTO.get_id());

        if (purchaseOrder == null) {
            throw new PurchaseOrderNotFoundException("Purchase order not found");
        }

        purchaseOrder.closePurchaseOrder();

        purchaseOrderRepository.flush();

        PurchaseOrderDTO updatedDTO = purchaseOrderAssembler.toResource(purchaseOrder);

        return updatedDTO;
    }

    public PurchaseOrderDTO dispatchPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(purchaseOrderDTO.get_id());

        if (purchaseOrder == null) {
            throw new PurchaseOrderNotFoundException("Purchase order not found");
        }

        purchaseOrder.dispatchPurchaseOrder();

        purchaseOrderRepository.save(purchaseOrder);

        PurchaseOrderDTO updatedDTO = purchaseOrderAssembler.toResource(purchaseOrder);

        return updatedDTO;

    }

    public PurchaseOrderDTO deliverPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(purchaseOrderDTO.get_id());

        if (purchaseOrder == null) {
            throw new PurchaseOrderNotFoundException("Purchase order not found");
        }

        purchaseOrder.deliverPurchaseOrder();

        purchaseOrderRepository.save(purchaseOrder);

        PurchaseOrderDTO updatedDTO = purchaseOrderAssembler.toResource(purchaseOrder);

        return updatedDTO;

    }

    public PurchaseOrderDTO returnPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(purchaseOrderDTO.get_id());

        if (purchaseOrder == null) {
            throw new PurchaseOrderNotFoundException("Purchase order not found");
        }

        purchaseOrder.returnPurchaseOrder();

        purchaseOrderRepository.save(purchaseOrder);

        PurchaseOrderDTO updatedDTO = purchaseOrderAssembler.toResource(purchaseOrder);

        Invoice invoice = this.createInvoice(purchaseOrder);

        this.sendInvoice(invoice);

        return updatedDTO;
    }

    private Invoice createInvoice(PurchaseOrder purchaseOrder) {
        Invoice invoice = new Invoice(IdentifierFactory.nextID(), false, purchaseOrder.getIssueDate().plusMonths(1), null, purchaseOrder.getTotal(), purchaseOrder);
        invoiceRepository.save(invoice);

        return invoice;
    }

    public List<PurchaseOrderDTO> getAllDeliveryPlants(LocalDate date) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAllPurchaseOrdersByStartDate(date);
        return purchaseOrderAssembler.toResources(purchaseOrders);

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

    public void acceptRemittance(RemittanceRestController.RemittanceAdviceDTO remittance) throws InvoiceNotFoundException {
        Invoice invoice = invoiceRepository.findOne(remittance.getInvoiceId());
        if (invoice == null) {
            throw new InvoiceNotFoundException("Invoice not found");
        }
        invoice.setPaid();
    }
}
