package com.example.models;

import java.time.LocalDate;

// TODO 2: Annotate this class with JPA/LOMBOK related metadata
// Recall that this class must be specified as a Value object! Use @Embeddable
public class BusinessPeriod {
    LocalDate startDate;
    LocalDate endDate;
}
