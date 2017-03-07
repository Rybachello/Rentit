package com.example.sales.domain.web.controller.dto;

import com.example.common.application.BusinessPeriodDTO;
import lombok.Data;

/**
 * Created by Rybachello on 3/3/2017.
 */
@Data
public class CatalogQueryDTO {
    String name;
    BusinessPeriodDTO rentalPeriod;
}