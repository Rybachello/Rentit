package com.example.common.domain.validation;

import com.example.common.domain.model.BusinessPeriod;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

/**
 * Created by Rybachello on 3/11/2017.
 */
public class BusinessPeriodValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return BusinessPeriod.class.equals(clazz);
    }

    public void validate(Object object, Errors errors) {
        BusinessPeriod period = (BusinessPeriod) object;
        LocalDate now = LocalDate.now();
        LocalDate far = LocalDate.ofYearDay(2222, 1);

        if (period.getStartDate().isAfter(period.getEndDate())){
            errors.reject("End date should be after the start date");
        }

        if (period.getStartDate().isBefore(now)||
                period.getEndDate().isBefore(now)||
                period.getStartDate().isAfter(far)||
                period.getEndDate().isAfter(far)){
            errors.reject("Dates should be between now and year 2222");
        }
    }
}
