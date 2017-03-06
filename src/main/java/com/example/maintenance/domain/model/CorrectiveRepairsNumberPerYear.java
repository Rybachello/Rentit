package com.example.maintenance.domain.model;

import lombok.Data;

/**
 * Created by Rybachello on 2/22/2017.
 */
@Data
public class CorrectiveRepairsNumberPerYear {

    Integer year;
    Long number;

    public CorrectiveRepairsNumberPerYear(Integer year, Long number ) {
        this.year = year;
        this.number = number;
    }
}
