package com.example.maintenance.application.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.sales.domain.model.TypeOfWork;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by Rybachello on 5/21/2017.
 */
@Data
public class MaintenanceDTO {

    String plantId;
    BusinessPeriodDTO maintenancePeriod;
    String description;
    @Enumerated(EnumType.STRING)
    TypeOfWork typeOfWork;
}
