package com.example.inventory.application.services;

import com.example.common.application.exceptions.InventoryEntryNotFoundException;
import com.example.common.rest.ExtendedLink;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.rest.controller.InventoryRestController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by minhi_000 on 08.03.2017.
 */

@Service
public class PlantInventoryEntryAssembler extends ResourceAssemblerSupport<PlantInventoryEntry, PlantInventoryEntryDTO> {
    public PlantInventoryEntryAssembler() {
        super(InventoryRestController.class, PlantInventoryEntryDTO.class);
    }

    @Override
    public PlantInventoryEntryDTO toResource(PlantInventoryEntry plant){
        PlantInventoryEntryDTO dto = createResourceWithId(plant.getId(), plant);
        dto.set_id(plant.getId());
        dto.setDescription(plant.getDescription());
        dto.setName(plant.getName());
        dto.setPrice(plant.getPrice());

        try {
            dto.add(new ExtendedLink(linkTo(methodOn(InventoryRestController.class).findPlantEntry(dto.get_id())).toString(),
                    "self", HttpMethod.GET));
        } catch (InventoryEntryNotFoundException e) {
            e.printStackTrace();
        }

        return dto;
    }
}
