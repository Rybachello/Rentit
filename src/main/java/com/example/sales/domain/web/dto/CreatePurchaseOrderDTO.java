package com.example.sales.domain.web.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.sales.domain.model.POStatus;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by minhi_000 on 08.03.2017.
 */
@Data
public class CreatePurchaseOrderDTO {
    private String plantId;
    private BigDecimal price;
    private BusinessPeriodDTO rentalPeriod;
    private POStatus status;
}
