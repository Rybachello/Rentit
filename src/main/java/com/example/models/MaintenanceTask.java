package com.example.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Vasiliy on 2017.02.20.
 */

@Entity
@Data
public class MaintenanceTask {
    @Id @GeneratedValue
    Long id;

    String description;

    @Column(precision=8,scale=2)
    BigDecimal price;

    @Enumerated(EnumType.STRING)
    TypeOfWork typeOfWork;

    @ManyToOne
    PlantReservation reservation;
}
