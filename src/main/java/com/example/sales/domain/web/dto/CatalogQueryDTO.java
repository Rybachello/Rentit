package com.example.sales.domain.web.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.domain.model.BusinessPeriod;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Rybachello on 3/3/2017.
 */
@Data
@AllArgsConstructor(staticName = "of")
public class CatalogQueryDTO {
    String name;
    BusinessPeriodDTO rentalPeriod;

    public CatalogQueryDTO() {
    }
}