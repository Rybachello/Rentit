package com.example.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
@Value
@NoArgsConstructor(force=true, access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class BusinessPeriod {
    LocalDate startDate;
    LocalDate endDate;
}
