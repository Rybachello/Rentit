package com.example.common.application.dto;

import com.example.common.rest.ResourceSupport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by Rybachello on 3/3/2017.
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
public class BusinessPeriodDTO extends ResourceSupport{
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate;

}

