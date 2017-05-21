package com.example.maintenance.application.dto;

import com.example.common.domain.model.BusinessPeriod;
import com.example.sales.domain.model.TypeOfWork;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by Rybachello on 5/21/2017.
 */
public class MaintenanceDTO {

    String plantId;
    BusinessPeriod maintenancePeriod;
    String description;
    @Enumerated(EnumType.STRING)
    TypeOfWork typeOfWork;
}
