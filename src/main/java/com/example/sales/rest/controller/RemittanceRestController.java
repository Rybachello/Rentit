package com.example.sales.rest.controller;

import com.example.common.application.exceptions.InvoiceNotFoundException;
import com.example.common.rest.ResourceSupport;
import com.example.sales.application.dto.RemittanceAdviceDTO;
import com.example.sales.application.services.SalesService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by stepan on 16/05/2017.
 */
@RestController
@RequestMapping("/api/remittances")
public class RemittanceRestController {
    @Autowired
    SalesService salesService;

    @ExceptionHandler(InvoiceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleInvoiceNotFoundException(InvoiceNotFoundException ex) {
    }

    @PostMapping("remittance")
    public void receiveRemittance(@RequestBody RemittanceAdviceDTO remittance) throws InvoiceNotFoundException {
        salesService.acceptRemittance(remittance);
    }
}
