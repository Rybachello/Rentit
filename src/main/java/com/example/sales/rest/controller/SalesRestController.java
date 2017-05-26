package com.example.sales.rest.controller;

import com.example.common.application.dto.ErrorDTO;
import com.example.common.application.exceptions.*;
import com.example.sales.application.dto.CustomerDTO;
import com.example.sales.application.dto.InvoiceDTO;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.services.CustomerService;
import com.example.sales.application.services.InvoiceService;
import com.example.sales.application.services.SalesService;
import com.example.sales.domain.model.Customer;
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
@CrossOrigin()
@RequestMapping("/api/sales")
@CrossOrigin
public class SalesRestController {
    @Autowired
    SalesService salesService;
    @Autowired
    CustomerService customerService;
    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    public List<PurchaseOrderDTO> fetchAllPurchaseOrders(@RequestHeader String token) {
        return salesService.getAllPurchaseOrders(token);
    }

    @GetMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.OK)

    public PurchaseOrderDTO fetchPurchaseOrder(@PathVariable("id") String id, @RequestHeader String token) throws PurchaseOrderNotFoundException, CustomerNotFoundException {
        Customer customer = customerService.findByToken(token);

        return salesService.getPurchaseOrderById(id, customer);
    }

    @ExceptionHandler(PlantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDTO> handPlantNotFoundException(PlantNotFoundException ex) {
        ErrorDTO errorDTO = ErrorDTO.of(ex.getMessage());
        return new ResponseEntity<ErrorDTO>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PurchaseOrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDTO> handPurchaseOrderNotFoundException(PurchaseOrderNotFoundException ex) {
        ErrorDTO errorDTO = ErrorDTO.of(ex.getMessage());
        return new ResponseEntity<ErrorDTO>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlantNotAvailableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorDTO> handPlantNotAvailableException(PlantNotAvailableException ex) {
        ErrorDTO errorDTO = ErrorDTO.of(ex.getMessage());
        return new ResponseEntity<ErrorDTO>(errorDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPurchaseOrderStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handInvalidPurchaseOrderStatusException(InvalidPurchaseOrderStatusException ex) {
        ErrorDTO errorDTO = ErrorDTO.of(ex.getMessage());
        return new ResponseEntity<ErrorDTO>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDTO> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ErrorDTO errorDTO = ErrorDTO.of(ex.getMessage());
        return new ResponseEntity<ErrorDTO>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomerExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorDTO> handPlantNotFoundException(CustomerExistException ex) {
        ErrorDTO errorDTO = ErrorDTO.of(ex.getMessage());
        return new ResponseEntity<ErrorDTO>(errorDTO, HttpStatus.CONFLICT);
    }

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO, @RequestHeader String token) throws PlantNotAvailableException, InvalidPurchaseOrderStatusException, CustomerNotFoundException, PlantNotFoundException {

        Customer customer = customerService.findByToken(token);

        PurchaseOrderDTO newPO = salesService.createPurchaseOrder(partialPODTO, customer);

        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create(newPO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(newPO, headers, HttpStatus.CREATED);
    }

    @PostMapping("/customer")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestParam(name = "email") String email) throws CustomerExistException {
        CustomerDTO newCustomer = customerService.createCustomer(email);

        HttpHeaders headers = new HttpHeaders();
        //   headers.setLocation(URI.create(newCustomer.getId().getHref()));

        return new ResponseEntity<CustomerDTO>(newCustomer, headers, HttpStatus.CREATED);
    }

    @PutMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> updatePurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO, @RequestHeader String token) throws PlantNotAvailableException, InvalidPurchaseOrderStatusException, CustomerNotFoundException {
        Customer customer = customerService.findByToken(token);

        PurchaseOrderDTO newPO = salesService.updatePurchaseOrder(partialPODTO, customer);

        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create(newPO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(newPO, headers, HttpStatus.CREATED);
    }

    @PostMapping("/orders/{id}/accept")
    public ResponseEntity<PurchaseOrderDTO> acceptPurchaseOrder(@PathVariable String id) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.acceptPurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{id}/accept")
    public ResponseEntity<PurchaseOrderDTO> rejectPurchaseOrder(@PathVariable String id) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.rejectPurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/rejectByCustomer")
    public ResponseEntity<PurchaseOrderDTO> rejectPOByCustomer(@PathVariable String id, @RequestHeader String token) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException, CustomerNotFoundException {
        Customer customer = customerService.findByToken(token);

        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id, customer);

        PurchaseOrderDTO updatedDTO = salesService.rejectPOByCustomer(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/dispatch")
    public ResponseEntity<PurchaseOrderDTO> dispatchPurchaseOrder(@PathVariable String id) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.dispatchPurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/deliver")
    public ResponseEntity<PurchaseOrderDTO> deliverPurchaseOrder(@PathVariable String id) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {

        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.deliverPurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/return")
    public ResponseEntity<PurchaseOrderDTO> returnPurchaseOrder(@PathVariable String id) throws PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.returnPurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<PurchaseOrderDTO> cancelPurchaseOrder(@PathVariable String id) throws Exception, PurchaseOrderNotFoundException, InvalidPurchaseOrderStatusException {
        PurchaseOrderDTO purchaseOrder = salesService.getPurchaseOrderById(id);

        PurchaseOrderDTO updatedDTO = salesService.cancelPurchaseOrder(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<PurchaseOrderDTO>(updatedDTO, headers, HttpStatus.OK);
    }

    @GetMapping("/deliveries")
    public List<PurchaseOrderDTO> getDeliveryPlants(@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return salesService.getAllDeliveryPlants(startDate);
    }

    @GetMapping("/invoices")
    public List<InvoiceDTO> getInvoices() {
        return invoiceService.getAllInvoices();
    }

}