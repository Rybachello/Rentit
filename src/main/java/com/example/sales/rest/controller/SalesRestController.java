package com.example.sales.rest.controller;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.application.exceptions.PlantNotAvailableException;
import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.application.services.InventoryService;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.services.SalesService;
import com.example.sales.domain.web.dto.CatalogQueryDTO;
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
    SalesService salesService;

    @GetMapping("/plants")
    public List<PlantInventoryEntryDTO> findAvailablePlants(
            @RequestParam(name = "name") String plantName,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        CatalogQueryDTO catalogQueryDTO = CatalogQueryDTO.of(plantName, BusinessPeriodDTO.of(startDate, endDate));
        return inventoryService.createListOfAvailablePlants(catalogQueryDTO);
    }

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    public List<PurchaseOrderDTO> fetchAllPurchaseOrders() throws PlantNotFoundException {
        //todo: do we need PlantNotFoundException here?
        return salesService.getAllPurchaseOrders();
    }

    @GetMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseOrderDTO fetchPurchaseOrder(@PathVariable("id") String id) throws PurchaseOrderNotFoundException {
        return salesService.getPurchaseOrderById(id);
    }

    @ExceptionHandler(PlantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handPlantNotFoundException(PlantNotFoundException ex) {
    }

    @ExceptionHandler(PurchaseOrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handPurchaseOrderNotFoundException(PurchaseOrderNotFoundException ex) {
    }

    @ExceptionHandler(PlantNotAvailableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handPlantNotAvailableException(PlantNotAvailableException ex) {}

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO) throws PlantNotAvailableException {
        PurchaseOrderDTO newPO = salesService.getPurchaseOrder(partialPODTO);
        //PurchaseOrderDTO newlyCreatePODTO = ...
        // TODO: Complete this part

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(newPO.getId().getHref()));
        // The above line won't working until you update PurchaseOrderDTO to extend ResourceSupport

        return new ResponseEntity<PurchaseOrderDTO>(newPO, headers, HttpStatus.CREATED);
    }
}