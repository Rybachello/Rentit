package com.example.sales.application.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Rybachello on 3/7/2017.
 */
@Data
public class PurchaseOrderDTO {
    private String id;
    private String name;
    private String status;
    private BusinessPeriodDTO rentalPeriod;
    private BigDecimal total;
    private String description;
}
