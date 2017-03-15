package com.example.sales.application.services;

import com.example.common.application.exceptions.PlantNotAvailableException;
import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.application.services.BusinessPeriodDisassembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.inventory.infrastructure.IdentifierFactory;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.CustomPurchaseOrderRepository;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
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
    CustomPurchaseOrderRepository purchaseOrderRepositoryImp;


    public PurchaseOrderDTO getPurchaseOrder(PurchaseOrderDTO dto) throws PlantNotAvailableException {

        //find first purchase order
        PlantInventoryEntry plantInventoryEntry = plantInventoryEntryRepository.findOne(dto.getPlant().get_id());
        //convert to dto
        BusinessPeriod businessPeriod = businessPeriodDisassembler.toResources(dto.getRentalPeriod());
        //create the purchase order with PENDING STATUS
        PurchaseOrder po = PurchaseOrder.of(IdentifierFactory.nextID(), LocalDate.now(), businessPeriod, plantInventoryEntry);

//todo: uncomment here
//        DataBinder binder = new DataBinder(po);
//        binder.addValidators(new PurchaseOrderValidator(new BusinessPeriodValidator()));
//        binder.validate();
//
//        if (binder.getBindingResult().hasErrors())
//            throw new BindException(binder.getBindingResult());

        try {
            PlantReservation plantReservation = inventoryService.createPlantReservation(dto.getPlant().get_id(), businessPeriod, po);
            po.confirmReservation(plantReservation, plantInventoryEntry.getPrice());
            //save to the database
            purchaseOrderRepository.save(po);
            plantReservationRepository.save(plantReservation);
        } catch (PlantNotAvailableException e) {
            po.rejectPuchaseOrder();
            purchaseOrderRepository.save(po);

            throw e;
        }


        PurchaseOrderDTO poDto = purchaseOrderAssembler.toResource(po);
        return poDto;
    }

    public List<PurchaseOrderDTO> getAllPurchaseOrders() throws PlantNotFoundException {
        List<PurchaseOrderDTO> purchaseOrderDTOs = purchaseOrderRepositoryImp.findAllPO();

        if (purchaseOrderDTOs.size() == 0) {
            throw new PlantNotFoundException("PurchaseOrder list is empty");
        }
        return purchaseOrderDTOs;
    }

    public PurchaseOrderDTO getPurchaseOrderById(String id) throws PurchaseOrderNotFoundException {
        try {
            return purchaseOrderRepositoryImp.findPurchaseOrderById(id);
        } catch (NoResultException e) {
            throw new PurchaseOrderNotFoundException("Purchase Order not found");
        }
    }
}
