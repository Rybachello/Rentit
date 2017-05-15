package com.example.sales.application.services;

import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.common.application.exceptions.PlantNotAvailableException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.application.services.BusinessPeriodDisassembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.domain.validation.BusinessPeriodValidator;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.inventory.infrastructure.IdentifierFactory;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.Customer;
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
    PurchaseOrderAssembler purchaseOrderAssembler;
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    BusinessPeriodDisassembler businessPeriodDisassembler;

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

    public PurchaseOrderDTO updatePurchaseOrder(PurchaseOrderDTO dto) throws PlantNotAvailableException, InvalidPurchaseOrderStatusException {
        BusinessPeriod businessPeriod = businessPeriodDisassembler.toResources(dto.getRentalPeriod());
        PurchaseOrder po = purchaseOrderRepository.findOne(dto.get_id());
        po = inventoryService.updatePlantReservation(po, businessPeriod);
        purchaseOrderRepository.flush();
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

        return updatedDTO;
    }

    public List<PurchaseOrderDTO> getAllDeliveryPlants(LocalDate date) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAllPurchaseOrdersByStartDate(date);
        return purchaseOrderAssembler.toResources(purchaseOrders);

    }
}
