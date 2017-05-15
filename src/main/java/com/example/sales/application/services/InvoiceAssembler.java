package com.example.sales.application.services;

import com.example.sales.application.dto.InvoiceDTO;
import com.example.sales.domain.model.Invoice;
import com.example.sales.domain.repository.InvoiceRepository;
import com.example.sales.rest.controller.SalesRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpMethod.POST;

/**
 * Created by stepan on 15/05/2017.
 */
@Service
public class InvoiceAssembler extends ResourceAssemblerSupport<Invoice, InvoiceDTO> {
    public InvoiceAssembler() {
        super(SalesRestController.class, InvoiceDTO.class);
    }

    @Override
    public InvoiceDTO toResource(Invoice invoice) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.set_id(invoice.getId());
        invoiceDTO.setOrderId(invoice.getPurchaseOrder().getId());
        invoiceDTO.setDueDate(invoice.getDueDate());
        invoiceDTO.setAmount(invoice.getAmount());

        return invoiceDTO;
    }
}
