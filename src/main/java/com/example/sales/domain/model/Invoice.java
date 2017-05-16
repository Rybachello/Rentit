package com.example.sales.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by stepan on 15/05/2017.
 */
@Data
@Entity
@AllArgsConstructor
public class Invoice {
    @Id
    String id;

    boolean paid;

    LocalDate dueDate;

    LocalDate paidDate;

    BigDecimal amount;

    @OneToOne
    PurchaseOrder purchaseOrder;

    public Invoice(){}
}
