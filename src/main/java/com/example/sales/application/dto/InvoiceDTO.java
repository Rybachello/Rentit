package com.example.sales.application.dto;

import com.example.common.rest.ResourceSupport;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by stepan on 15/05/2017.
 */
@Data
@EqualsAndHashCode
public class InvoiceDTO extends ResourceSupport {
    String _id;
    String orderId;
    LocalDate dueDate;
    BigDecimal amount;
    boolean paid;
}
