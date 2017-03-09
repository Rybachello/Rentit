package com.example.sales.application.services;

import com.example.common.application.BusinessPeriodDTO;
import com.example.inventory.application.PurchaseOrderDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import org.springframework.stereotype.Service;

/**
 * Created by minhi_000 on 09.03.2017.
 */
@Service
public class PurchaseOrderAssembler {
    public PurchaseOrderDTO toResource(PlantInventoryEntry plant, BusinessPeriodDTO period){
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setId(plant.getId());
        dto.setName(plant.getName());
        dto.setTotal(plant.getPrice());
        dto.setRentalPeriod(period);
        dto.setDescription(plant.getDescription());
        return dto;
    }
}
