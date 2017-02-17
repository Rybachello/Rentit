package com.example.models;

// TODO 1: Annotate this class with JPA & LOMBOK relevant information
public class PlantInventoryItem {
    // TODO 1.1: Mark this property as @Id and specify that you want JPA to use an ID generator
    Long id;
    String serialNumber;
    // TODO 1.2: Select the format used for storing the enumeration (i.e. integer vs. string)
    EquipmentCondition equipmentCondition;
    // TODO 1.3: Specify the cardinality and direction of the relationship
    PlantInventoryEntry plantInfo;
}
