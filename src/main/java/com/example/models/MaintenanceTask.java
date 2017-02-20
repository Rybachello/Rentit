package com.example.models;

import lombok.Data;

import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.math.BigDecimal;

/**
 * Created by Vasiliy on 2017.02.20.
 */

@Entity
@Data
public class MaintenanceTask {
    @Id @GeneratedValue
    Long Id;

    String Description;

    BigDecimal Price;

    @Embedded
    BusinessPeriod Period;
}
