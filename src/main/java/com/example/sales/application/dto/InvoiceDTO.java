package com.example.sales.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor(staticName = "of")
public class InvoiceDTO {
    String _id;
    String orderId;
    BigDecimal amount;
    LocalDate dueDate;
}
