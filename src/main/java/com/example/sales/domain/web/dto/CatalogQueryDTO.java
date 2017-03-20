package com.example.sales.domain.web.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.rest.ResourceSupport;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Rybachello on 3/3/2017.
 */
@Data
@AllArgsConstructor(staticName = "of")
public class CatalogQueryDTO extends ResourceSupport{
    String name;
    BusinessPeriodDTO rentalPeriod;

    public CatalogQueryDTO() {
    }
}