package com.example.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Embeddable;
import java.time.LocalDate;

// TODO 2: Annotate this class with JPA/LOMBOK related metadata
// Recall that this class must be specified as a Value object! Use @Embeddable
@Embeddable
@Value
@NoArgsConstructor(force=true, access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class BusinessPeriod {
    LocalDate startDate;
    LocalDate endDate;
}
