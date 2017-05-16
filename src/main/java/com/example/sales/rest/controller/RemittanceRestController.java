package com.example.sales.rest.controller;

import com.example.common.application.dto.ErrorDTO;
import com.example.common.application.exceptions.InvoiceNotFoundException;
import com.example.sales.application.dto.RemittanceAdviceDTO;
import com.example.sales.application.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by stepan on 16/05/2017.
 */
@RestController
@RequestMapping("/api/remittances")
public class RemittanceRestController {
    @Autowired
    InvoiceService invoiceService;

    @ExceptionHandler(InvoiceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDTO> handleInvoiceNotFoundException(InvoiceNotFoundException ex) {
        ErrorDTO errorDTO = ErrorDTO.of(ex.getMessage());
        return new ResponseEntity<ErrorDTO>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @PostMapping("remittance")
    public void receiveRemittance(@RequestBody RemittanceAdviceDTO remittance) throws InvoiceNotFoundException {
        invoiceService.acceptRemittance(remittance);
    }
}
