package com.example.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class PlantReservation {
    @Id @GeneratedValue
    Long id;

    @Embedded
    BusinessPeriod schedule;

    @ManyToOne
    PlantInventoryItem plant;

    @ManyToOne
    MaintenancePlan maintenancePlan;

    @ManyToOne
    PurchaseOrder rental;
}
