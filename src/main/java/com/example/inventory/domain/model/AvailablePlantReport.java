package com.example.inventory.domain.model;
import lombok.Data;

@Data
public class AvailablePlantReport {
   String name;
   String description;
   Long amount;

    public AvailablePlantReport(String name, String description, long amount ){
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

}

