package com.example.sales.domain.web.controller.dto;

import com.example.common.application.BusinessPeriodDTO;
import com.example.sales.domain.model.POStatus;
import lombok.Data;

import java.time.LocalDate;

/**
 * Created by minhi_000 on 08.03.2017.
 */
@Data
public class CreatePurchaseOrderDTO {
    private String plantId;
    private BusinessPeriodDTO rentalPeriod;
    private POStatus status;
}
