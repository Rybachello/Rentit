package com.example.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Vasiliy on 2017.02.20.
 */
@Entity
@Data
public class PurchaseOrder {
    @Id
    @GeneratedValue
    Long id;

    @OneToMany
    List<PlantReservation> reservations;

    @ManyToOne
    PlantInventoryEntry plant;

    LocalDate issueDate;

    LocalDate paymentSchedule;

    @Column(precision=8,scale=2)
    BigDecimal total;

    @Enumerated(EnumType.STRING)
    POStatus status;

    @Embedded
    BusinessPeriod period;
}