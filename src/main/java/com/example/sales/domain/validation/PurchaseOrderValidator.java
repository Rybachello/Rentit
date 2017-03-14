package com.example.sales.domain.validation;

import com.example.common.domain.model.BusinessPeriod;
import com.example.common.domain.validation.BusinessPeriodValidator;
import com.example.sales.domain.model.POStatus;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by Rybachello on 3/11/2017.
 */
public class PurchaseOrderValidator implements Validator {
    private final BusinessPeriodValidator periodValidator;

    public PurchaseOrderValidator(BusinessPeriodValidator periodValidator) {
        if (periodValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is " +
                    "required and must not be null.");
        }
        if (!periodValidator.supports(BusinessPeriod.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must " +
                    "support the validation of [BusinessPeriod] instances.");
        }
        this.periodValidator = periodValidator;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PurchaseOrder.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PurchaseOrder order = (PurchaseOrder) o;
        if (order.getId() == null)
            errors.reject("id", "Purchase Order id cannot be null");

        if ()


        errors.pushNestedPath("rentalPeriod");
        ValidationUtils.invokeValidator(periodValidator, order.getRentalPeriod(), errors);
        errors.popNestedPath();
    }
}