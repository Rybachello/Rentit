package com.example.sales.application.services;

import com.example.inventory.application.PurchaseOrderDTO;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.sales.domain.web.controller.dto.CreatePurchaseOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Rybachello on 3/7/2017.
 */
@Service
public class SalesService {
    @Autowired
    PlantInventoryEntryRepository repo;
    @Autowired
    PlantInventoryEntryAssembler ass;
    public PurchaseOrderDTO getPurchaseOrder(CreatePurchaseOrderDTO dto){
        PlantInventoryEntry plant = repo.findOne(dto.getPlantId());
        PurchaseOrderDTO poDto = ass.toResource(plant, dto.getRentalPeriod());
        return poDto;
    }
}
