package com.example.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

// TODO 3: Annotate this class with JPA & LOMBOK relevant information
@Entity
@Data
public class PlantReservation {
    // TODO 3.1: Mark this property as @Id and specify that you want JPA to use an ID generator
    @Id @GeneratedValue
    Long id;
    // TODO 3.2: Refactor these two properties to BusinessPeriod (Since BusinessPeriod is a value object, you must declare the new property as Embedded)
    @Embedded
    BusinessPeriod schedule;
    // TODO 3.3: Specify the cardinality and direction of the relationship
    @ManyToOne
    PlantInventoryItem plant;
}
