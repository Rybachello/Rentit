package com.example.inventory.application.services;

import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by minhi_000 on 08.03.2017.
 */

@Service
public class PlantInventoryEntryAssembler extends ResourceAssemblerSupport<PlantInventoryEntry, PlantInventoryEntryDTO> {
    public PlantInventoryEntryAssembler() {
        super(PlantInventoryEntry.class, PlantInventoryEntryDTO.class);
    }

    public PlantInventoryEntryDTO toResource(PlantInventoryEntry plant){
        PlantInventoryEntryDTO dto = createResourceWithId(plant.getId(), plant);
        dto.set_id(plant.getId());
        dto.setDescription(plant.getDescription());
        dto.setName(plant.getName());
        dto.setPrice(plant.getPrice());
        return dto;
    }
    //todo:remove this method
    public List<PlantInventoryEntryDTO> toResources(List<PlantInventoryEntry> plants){
        return plants.stream().map(p -> toResource(p)).collect(Collectors.toList());
    }
}
