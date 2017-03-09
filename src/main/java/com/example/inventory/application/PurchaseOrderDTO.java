package com.example.inventory.application;

import com.example.common.application.BusinessPeriodDTO;
import com.example.sales.domain.model.POStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Rybachello on 3/7/2017.
 */
@Data
public class PurchaseOrderDTO {
    private String id;
    private String name;
    private String status;
    private String description;
    private BusinessPeriodDTO rentalPeriod;
    private BigDecimal total;
}
