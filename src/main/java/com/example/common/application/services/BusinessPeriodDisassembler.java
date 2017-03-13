package com.example.common.application.services;


import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.domain.model.BusinessPeriod;
import org.springframework.stereotype.Service;

/**
 * Created by Rybachello on 3/11/2017.
 */
@Service
public class BusinessPeriodDisassembler {
    public BusinessPeriod toResources(BusinessPeriodDTO businessPeriodDTO)
    {
        return BusinessPeriod.of(businessPeriodDTO.getStartDate(),businessPeriodDTO.getEndDate());
    }
}
