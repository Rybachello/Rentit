package com.example.sales.application.services;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.stereotype.Service;

/**
 * Created by minhi_000 on 09.03.2017.
 */
@Service
public class PurchaseOrderAssembler {
    public PurchaseOrderDTO toResource(PlantInventoryEntry plant, BusinessPeriodDTO period, PurchaseOrder po){
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setId(plant.getId());
        dto.setName(plant.getName());
        dto.setTotal(po.getTotal());
        dto.setRentalPeriod(period);
        dto.setDescription(plant.getDescription());
        dto.setStatus(po.getStatus().toString());
        return dto;
    }
}
