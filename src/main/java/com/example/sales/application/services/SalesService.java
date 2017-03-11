package com.example.sales.application.services;

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

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Rybachello on 3/7/2017.
 */
@Service
public class SalesService {
    @Autowired
    PlantInventoryEntryRepository repo;
    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;
    //@Autowired
    //PlantInventoryItemRepository plantInventoryItem;
    @Autowired
    InventoryService inventoryService;


    public PurchaseOrderDTO getPurchaseOrder(CreatePurchaseOrderDTO dto){
        PlantInventoryEntry plant = repo.findOne(dto.getPlantId());
        BigDecimal total = plant.getPrice();
        BigDecimal days = BigDecimal.valueOf(dto.getRentalPeriod().getDurationDays());
        total = total.multiply(days);
        PurchaseOrder po = PurchaseOrder.of(IdentifierFactory.nextID(), LocalDate.now(),null, total, POStatus.PENDING);

        try {
            PlantReservation plantReservation = inventoryService.reserveItem(dto.getPlantId(), dto.getRentalPeriod(),po.getId());
        } catch (InventoryService.NoPlantAvailableException e) {
                po.rejectPo();
        }

        purchaseOrderRepository.save(po);
        PurchaseOrderDTO poDto = purchaseOrderAssembler.toResource(plant,dto.getRentalPeriod(), po);
        return poDto;
    }
}
