package com.example.models;
import lombok.Data;

@Data
public class AvaliablePlantReport {
   String name;
   String description;
   Long amount;

    public AvaliablePlantReport (java.lang.String name, java.lang.String description, long amount ){
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

}

