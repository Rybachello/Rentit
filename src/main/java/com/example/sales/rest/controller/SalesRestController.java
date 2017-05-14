package com.example.sales.rest.controller;

import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.common.application.exceptions.PlantNotAvailableException;
import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.services.SalesService;

import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Created by Rybachello on 3/11/2017.
 */
@RestController
@RequestMapping("/api/sales")
public class SalesRestController {
    @Autowired
    SalesService salesService;

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    public List<PurchaseOrderDTO> fetchAllPurchaseOrders(){
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

    @ExceptionHandler(InvalidPurchaseOrderStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handInvalidPurchaseOrderStatusException(InvalidPurchaseOrderStatusException ex) {}

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO) throws PlantNotAvailableException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO newPO = salesService.createPurchaseOrder(partialPODTO);

        HttpHeaders headers = new HttpHeaders();
        //headers.setLocation(URI.create(newPO.getId().getHref()));
        // The above line won't working until you update PurchaseOrderDTO to extend ResourceSupport

        return new ResponseEntity<PurchaseOrderDTO>(newPO, headers, HttpStatus.CREATED);
    }

    @PutMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> updatePurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO) throws PlantNotAvailableException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO newPO = salesService.updatePurchaseOrder(partialPODTO);

        HttpHeaders headers = new HttpHeaders();
        //headers.setLocation(URI.create(newPO.getId().getHref()));
        // The above line won't working until you update PurchaseOrderDTO to extend ResourceSupport

        return new ResponseEntity<PurchaseOrderDTO>(newPO, headers, HttpStatus.CREATED);
    }

    @PostMapping("/orders/{id}/accept")
    public ResponseEntity<PurchaseOrderDTO> acceptPurchaseOrder(@PathVariable String id) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.acceptPurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(updatedDTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{id}/accept")
    public ResponseEntity<PurchaseOrderDTO> rejectPurchaseOrder(@PathVariable String id) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.rejectPurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(updatedDTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/rejectByCustomer")
    public ResponseEntity<PurchaseOrderDTO> rejectPOByCustomer(@PathVariable String id) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.rejectPOByCustomer(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(updatedDTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/dispatch")
    public ResponseEntity<PurchaseOrderDTO> dispatchPurchaseOrder(@PathVariable String id) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.dispatchPurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(updatedDTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/deliver")
    public ResponseEntity<PurchaseOrderDTO> deliverPurchaseOrder(@PathVariable String id) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.deliverPurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(updatedDTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/return")
    public ResponseEntity<PurchaseOrderDTO> returnPurchaseOrder(@PathVariable String id) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.returnPurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(updatedDTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<PurchaseOrderDTO> closePurchaseOrder(@PathVariable String id) throws Exception, PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.closePurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(updatedDTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }
}