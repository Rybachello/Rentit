package com.example.maintenance.domain.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Rybachello on 2/23/2017.
 */
@Data
public class CorrectiveRepairsCostPerYear {

    Integer year;
    BigDecimal cost;

    public CorrectiveRepairsCostPerYear(Integer year, BigDecimal cost) {
        this.year = year;
        this.cost = cost;
    }
}
