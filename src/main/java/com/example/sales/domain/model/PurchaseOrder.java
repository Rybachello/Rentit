package com.example.sales.domain.model;

import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Vasiliy on 2017.02.20.
 */
@Entity
@Getter
@NoArgsConstructor(force=true, access= AccessLevel.PRIVATE)
public class PurchaseOrder {
    @Id
    String id;

    LocalDate issueDate;

    @ManyToOne
    PlantInventoryItem plant;

    @ManyToOne
    PlantInventoryEntry entry;

    @Embedded
    BusinessPeriod rentalPeriod;

    @Column(precision=8,scale=2)
    BigDecimal total;

    @Enumerated(EnumType.STRING)
    POStatus status;

    //site where we need to deliver
    String constructionSite;

    public static PurchaseOrder of(String id, LocalDate issueDate, BusinessPeriod businessPeriod, PlantInventoryItem plant, PlantInventoryEntry entry,String constructionSite)
    {
        PurchaseOrder po = new PurchaseOrder();
        po.id = id;
        po.issueDate = issueDate;
        po.rentalPeriod = businessPeriod;
        po.plant = plant;
        po.status = POStatus.PENDING;
        po.constructionSite = constructionSite;
        return po;
    }

    public void confirmReservation(BigDecimal price) {
        total = price.multiply(BigDecimal.valueOf(rentalPeriod.numberOfWorkingDays()));
    }

    public void rejectPurchaseOrder() throws InvalidPurchaseOrderStatusException {
        if (this.status == POStatus.PENDING) {
            this.status = POStatus.REJECTED;
        } else {
            throw new InvalidPurchaseOrderStatusException("Purchase Order status must be PENDING to reject it");
        }
    }

    public void rejectByCustomer() throws InvalidPurchaseOrderStatusException {
        if(this.status == POStatus.DISPATCHED) {
            this.status = POStatus.REJECTED_BY_CUSTOMER;
        } else {
            throw new InvalidPurchaseOrderStatusException("Purchase Order status must be PENDING to reject it");
        }
    }

    public void acceptPurchaseOrder() throws InvalidPurchaseOrderStatusException {
        if (this.status == POStatus.PENDING) {
            this.status = POStatus.OPEN;
        } else {
            throw new InvalidPurchaseOrderStatusException("Purchase Order status must be PENDING to accept it.");
        }
    }

    public void dispatchPurchaseOrder() throws InvalidPurchaseOrderStatusException {
        if (this.status == POStatus.OPEN) {
            this.status = POStatus.DISPATCHED;
        } else {
            throw new InvalidPurchaseOrderStatusException("Purchase Order status must be OPEN to dispatch it.");
        }
    }

    public void deliverPurchaseOrder() throws InvalidPurchaseOrderStatusException {
        if (this.status == POStatus.DISPATCHED) {
            this.status = POStatus.DELIVERED;
        } else {
            throw new InvalidPurchaseOrderStatusException("Purchase Order status must be DISPATCHED to deliver it.");
        }
    }

    public void returnPurchaseOrder() throws InvalidPurchaseOrderStatusException {
        if (this.status == POStatus.DELIVERED) {
            this.status = POStatus.RETURNED;
        } else {
            throw new InvalidPurchaseOrderStatusException("Purchase Order status must be DELIVERED to return it.");
        }
    }

    public void closePurchaseOrder() throws InvalidPurchaseOrderStatusException {
        if (this.status == POStatus.OPEN) {
            this.status = POStatus.CLOSED;
        } else {
            throw new InvalidPurchaseOrderStatusException("Purchase Order status must be OPEN to close it.");
        }
    }

    public void updateRentalPeriod(BusinessPeriod period) throws InvalidPurchaseOrderStatusException {
        if (this.status == POStatus.OPEN || this.status == POStatus.PENDING) {
            this.status = POStatus.PENDING;
            this.rentalPeriod = period;
        } else {
            throw new InvalidPurchaseOrderStatusException("Purchase Order can be changed only when opened or pending");
        }
    }

    public void confirm(BusinessPeriod rentalPeriod, PlantInventoryItem item) {
        this.rentalPeriod = rentalPeriod;
        this.plant = item;
    }
}