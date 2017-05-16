package com.example.inventory.rest.controller;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.application.dto.ErrorDTO;
import com.example.common.application.exceptions.InventoryEntryNotFoundException;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.application.dto.CatalogQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(InventoryEntryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDTO> handleInventoryEntryNotFoundException(InventoryEntryNotFoundException ex) {
        ErrorDTO errorDTO = ErrorDTO.of(ex.getMessage());
        return new ResponseEntity<ErrorDTO>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/plants/{id}")
    public PlantInventoryEntryDTO findPlantEntry(@PathVariable String id) throws InventoryEntryNotFoundException {
        return inventoryService.getEntryById(id);
    }

}
