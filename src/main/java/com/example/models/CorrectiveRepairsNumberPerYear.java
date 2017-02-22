package com.example.models;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Rybachello on 2/22/2017.
 */
@Data
public class CorrectiveRepairsNumberPerYear {

    Integer year;
    Long number;

    public CorrectiveRepairsNumberPerYear(Integer year, Long number )
    {
        this.year = year;
        this.number = number;
    }



}
