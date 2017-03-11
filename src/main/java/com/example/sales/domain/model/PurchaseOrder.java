package com.example.sales.domain.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Vasiliy on 2017.02.20.
 */
@Entity
@Getter
@NoArgsConstructor(force=true, access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class PurchaseOrder {
    @Id
    String id;

    //todo: convert here to businessPeriod
    //businessPeriod rentalPeriod

    LocalDate issueDate;

    LocalDate paymentSchedule;

    @Column(precision=8,scale=2)
    BigDecimal total;

    @Enumerated(EnumType.STRING)
    POStatus status;

    public void rejectPo() {
        this.status = POStatus.REJECTED;
    }

}