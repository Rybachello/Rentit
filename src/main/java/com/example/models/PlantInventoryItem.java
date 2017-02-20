package com.example.models;

import lombok.Data;

import javax.persistence.*;

// TODO 1: Annotate this class with JPA & LOMBOK relevant information
@Entity
@Data
public class PlantInventoryItem {
    // TODO 1.1: Mark this property as @Id and specify that you want JPA to use an ID generator
    @Id @GeneratedValue
    Long id;
    String serialNumber;
    // TODO 1.2: Select the format used for storing the enumeration (i.e. integer vs. string)
    @Enumerated(EnumType.STRING)
    EquipmentCondition equipmentCondition;
    // TODO 1.3: Specify the cardinality and direction of the relationship
    @ManyToOne
    PlantInventoryEntry plantInfo;
}
