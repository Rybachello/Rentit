package com.example.sales.application.services;

import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.common.application.exceptions.PlantNotAvailableException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.application.services.BusinessPeriodDisassembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.domain.validation.BusinessPeriodValidator;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.inventory.infrastructure.IdentifierFactory;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.Invoice;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import com.example.sales.domain.validation.PurchaseOrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

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
    PlantReservationRepository plantReservationRepository;
    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    BusinessPeriodDisassembler businessPeriodDisassembler;
    @Autowired
    InvoiceService invoiceService;

    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO dto) throws PlantNotAvailableException, InvalidPurchaseOrderStatusException {

        //find first purchase order
        PlantInventoryEntry plantInventoryEntry = plantInventoryEntryRepository.findOne(dto.getPlant().get_id());
        //convert to dto
        BusinessPeriod businessPeriod = businessPeriodDisassembler.toResources(dto.getRentalPeriod());
        //create the purchase order with PENDING STATUS
        PurchaseOrder po = PurchaseOrder.of(IdentifierFactory.nextID(), LocalDate.now(), businessPeriod, plantInventoryEntry);


        try {
            PlantReservation plantReservation = inventoryService.createPlantReservation(dto.getPlant().get_id(), businessPeriod, po);
            po.confirmReservation(plantReservation, plantInventoryEntry.getPrice());
            //save to the database
            DataBinder binder = new DataBinder(po);
            binder.addValidators(new PurchaseOrderValidator(new BusinessPeriodValidator()));
            binder.validate();
            if (!binder.getBindingResult().hasErrors()) {
                purchaseOrderRepository.save(po);
                plantReservationRepository.save(plantReservation);
            }
        } catch (PlantNotAvailableException e) {
            po.rejectPuchaseOrder();
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

    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        return purchaseOrderAssembler.toResources(purchaseOrders);
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

        purchaseOrder.rejectPuchaseOrder();

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

        purchaseOrderRepository.save(purchaseOrder);

        Invoice invoice = invoiceService.createInvoice(purchaseOrder);
        try {
            invoiceService.sendInvoice(invoice, "rentit228@gmail.com");
        } catch (Exception e) {
            e.printStackTrace();
        }

        PurchaseOrderDTO updatedDTO = purchaseOrderAssembler.toResource(purchaseOrder);

        return updatedDTO;
    }
}
