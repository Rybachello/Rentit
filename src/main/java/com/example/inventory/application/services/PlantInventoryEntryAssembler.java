package com.example.inventory.application.services;

import com.example.common.application.BusinessPeriodDTO;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.PlantInventoryEntryDTO;
import com.example.inventory.application.PurchaseOrderDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by minhi_000 on 08.03.2017.
 */
@Service
public class PlantInventoryEntryAssembler {
    public PlantInventoryEntryDTO toResource(PlantInventoryEntry plant){
        PlantInventoryEntryDTO dto = new PlantInventoryEntryDTO();
        dto.setId(plant.getId());
        dto.setDescription(plant.getDescription());
        dto.setName(plant.getName());
        dto.setPrice(plant.getPrice());
        return dto;
    }

    public PurchaseOrderDTO toResource(PlantInventoryEntry plant, BusinessPeriodDTO period){
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setId(plant.getId());
        dto.setName(plant.getName());
        dto.setTotal(plant.getPrice());
        dto.setRentalPeriod(period);
        return dto;
    }

    public List<PlantInventoryEntryDTO> toResources(List<PlantInventoryEntry> plants){
        return plants.stream().map(p -> toResource(p)).collect(Collectors.toList());
    }
}
