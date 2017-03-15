package com.example.sales.domain.model;

import com.example.common.domain.model.BusinessPeriod;
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

    // to store reservation dates in case of PO rejection (plant reservation in this case will not be saved)
    @Embedded
    BusinessPeriod rentalPeriod;

    @Column(precision=8,scale=2)
    BigDecimal total;

    @Enumerated(EnumType.STRING)
    POStatus status;

    @OneToMany(mappedBy="purchaseOrder")
    private List<PlantReservation> reservations;

    public static PurchaseOrder of(String id, LocalDate issueDate)
    {
        PurchaseOrder po = new PurchaseOrder();
        po.id = id;
        po.issueDate = issueDate;
        po.status = POStatus.PENDING;
        return po;
    }

    public void confirmReservation(PlantReservation plantReservation, BigDecimal price) {
        //get period
        BusinessPeriod businessPeriod = plantReservation.getSchedule();
        this.rentalPeriod = businessPeriod;
        total = price.multiply(BigDecimal.valueOf(businessPeriod.numberOfWorkingDays()));
        status = POStatus.OPEN;
    }

    public void rejectPuchaseOrder() {
        this.status = POStatus.REJECTED;
    }


}