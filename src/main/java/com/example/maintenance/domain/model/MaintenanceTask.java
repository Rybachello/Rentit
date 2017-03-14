package com.example.maintenance.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantReservation;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Vasiliy on 2017.02.20.
 */

@Entity
@Getter
@NoArgsConstructor(force=true, access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class MaintenanceTask {
    @Id
    String id;

    String description;

    @Column(precision=8,scale=2)
    BigDecimal price;

    @Enumerated(EnumType.STRING)
    TypeOfWork typeOfWork;

    @ManyToOne
    @JoinColumn(name = "reservationId")
    PlantReservation reservation;

    @ManyToOne
    @JoinColumn(name = "maintenancePlanId")
    MaintenancePlan maintenancePlan;
}
