package com.example.sales.application.services;

import com.example.common.application.exсeptions.PlantNotFoundException;
import com.example.common.application.services.BusinessPeriodDisassembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.inventory.infrastructure.IdentifierFactory;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.sales.domain.model.POStatus;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import com.example.sales.domain.web.dto.CreatePurchaseOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import java.math.BigDecimal;
import java.time.LocalDate;

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



    public PurchaseOrderDTO getPurchaseOrder(CreatePurchaseOrderDTO dto) throws PlantNotFoundException{

        //find first purchase order
        PlantInventoryEntry plantInventoryEntry = plantInventoryEntryRepository.findOne(dto.getPlantId());
        //convert to dto
        BusinessPeriod businessPeriod = businessPeriodDisassembler.toResources(dto.getRentalPeriod());
        //create the purchase order with PENDING STATUS
        PurchaseOrder po = PurchaseOrder.of(IdentifierFactory.nextID(),LocalDate.now());

//todo: uncomment here
//        DataBinder binder = new DataBinder(po);
//        binder.addValidators(new PurchaseOrderValidator(new BusinessPeriodValidator()));
//        binder.validate();
//
//        if (binder.getBindingResult().hasErrors())
//            throw new BindException(binder.getBindingResult());

        try {
            PlantReservation plantReservation = inventoryService.createPlantReservation(dto.getPlantId(),businessPeriod,po.getId());
            po.confirmReservation(plantReservation,plantInventoryEntry.getPrice());
            //save to the database
            purchaseOrderRepository.save(po);
            plantReservationRepository.save(plantReservation);
        } catch (PlantNotFoundException e) {
            po.rejectPuchaseOrder();
            purchaseOrderRepository.save(po);
        }


        PurchaseOrderDTO poDto = purchaseOrderAssembler.toResource(plantInventoryEntry,dto.getRentalPeriod(), po);
        return poDto;
    }
}
