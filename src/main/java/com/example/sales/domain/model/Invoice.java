package com.example.sales.domain.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by Rybachello on 5/3/2017.
 */
@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Invoice {

    @Id
    String id;

    @OneToOne
    PurchaseOrder purchaseOrder;

    @OneToOne(cascade = CascadeType.ALL)
    Remittance remittance;

    public static Invoice of(String id, PurchaseOrder purchaseOrder) {
        Invoice invoice = new Invoice();

        invoice.id = id;
        invoice.purchaseOrder = purchaseOrder;

        return invoice;
    }

    public Invoice closeInvoice(Remittance remittance) {

        this.remittance = remittance;
        return this;
    }
}
