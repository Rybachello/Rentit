package com.example.sales.domain.model;

import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    LocalDate paymentSchedule;

    @ManyToOne
    PlantInventoryEntry plant;

    // to store reservation dates in case of PO rejection (plant reservation in this case will not be saved)
    @Embedded
    BusinessPeriod rentalPeriod;

    @Column(precision=8,scale=2)
    BigDecimal total;

    @Enumerated(EnumType.STRING)
    POStatus status;

    @OneToMany(mappedBy="purchaseOrder")
    private List<PlantReservation> reservations;

    public static PurchaseOrder of(String id, LocalDate issueDate, BusinessPeriod businessPeriod, PlantInventoryEntry plant)
    {
        PurchaseOrder po = new PurchaseOrder();
        po.id = id;
        po.issueDate = issueDate;
        po.rentalPeriod = businessPeriod;
        po.plant = plant;
        po.status = POStatus.PENDING;
        return po;
    }

    public void confirmReservation(PlantReservation plantReservation, BigDecimal price) {
        //get period
        BusinessPeriod businessPeriod = plantReservation.getSchedule();
        this.rentalPeriod = businessPeriod;
        total = price.multiply(BigDecimal.valueOf(businessPeriod.numberOfWorkingDays()));
    }

    public void rejectPurchaseOrder() throws InvalidPurchaseOrderStatusException {
        if (this.status == POStatus.PENDING) {
            this.status = POStatus.REJECTED;
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

}