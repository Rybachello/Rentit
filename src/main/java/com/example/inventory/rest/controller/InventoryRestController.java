package com.example.inventory.rest.controller;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.application.services.InventoryService;
import com.example.sales.domain.web.dto.CatalogQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Rybachello on 3/11/2017.
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryRestController {
    @Autowired
    InventoryService inventoryService;

    @GetMapping("/plants")
    public List<PlantInventoryEntryDTO> findAvailablePlants(
            @RequestParam(name = "name", required = false) String plantName,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        if (plantName == null)
            return inventoryService.allPlants();

        CatalogQueryDTO catalogQueryDTO = CatalogQueryDTO.of(plantName, BusinessPeriodDTO.of(startDate, endDate));
        return inventoryService.createListOfAvailablePlants(catalogQueryDTO);
    }

//    @GetMapping("/plants")
//    public List<PlantInventoryEntryDTO> findAvailablePlants() {
//        return inventoryService.allPlants();
//    }

    @GetMapping("/plants/{id}")
    public PlantInventoryEntryDTO findPlantEntry(@PathVariable String id) {
        return inventoryService.getEntryById(id);
    }

}
