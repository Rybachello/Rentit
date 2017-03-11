package com.example.sales.rest.controller;

import com.example.common.application.exсeptions.PlantNotFoundException;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.application.services.InventoryService;
import com.example.sales.application.dto.PurchaseOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Rybachello on 3/11/2017.
 */
@RestController
@RequestMapping("/api/sales")
public class SalesRestController {
    @Autowired
    InventoryService inventoryService;
    @Autowired
    InventoryService salesService;

    @GetMapping("/plants")
    public List<PlantInventoryEntryDTO> findAvailablePlants(
            @RequestParam(name = "name") String plantName,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        //todo: fixed query name
        return null;
        //return inventoryService.createListOfAvailablePlants(plantName,startDate,endDate);
    }

    @GetMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseOrderDTO fetchPurchaseOrder(@PathVariable("id") String id) {
        // TODO: Complete this part
        return null;
    }

    @ExceptionHandler(PlantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handPlantNotFoundException(PlantNotFoundException ex) {
    }

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO) {
        //PurchaseOrderDTO newlyCreatePODTO = ...
        // TODO: Complete this part

        HttpHeaders headers = new HttpHeaders();
        //headers.setLocation(new URI(newlyCreatePODTO.getId().getHref()));
        // The above line won't working until you update PurchaseOrderDTO to extend ResourceSupport

        //return new ResponseEntity<PurchaseOrderDTO>(newlyCreatePODTO, headers, HttpStatus.CREATED);
        return null;
    }
}