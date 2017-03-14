package com.example.sales.application.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.domain.model.BusinessPeriod;
import com.example.sales.domain.model.POStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by Rybachello on 3/7/2017.
 */
@Data
public class PurchaseOrderDTO {
    public PurchaseOrderDTO(){}

    public PurchaseOrderDTO(String id, String name, POStatus status, LocalDate startDate, LocalDate endDate, BigDecimal price, String description){
        this.id = id;
        this.name = name;
        this.status = status==null?"unknown":status.toString();
        this.description = description;
        this.rentalPeriod = BusinessPeriodDTO.of(startDate, endDate);
        this.total = BigDecimal.valueOf(ChronoUnit.DAYS.between(startDate, endDate) + 1).multiply(price);
    }

    private String id;
    private String name;
    private String status;
    private BusinessPeriodDTO rentalPeriod;
    private BigDecimal total;
    private String description;
}
