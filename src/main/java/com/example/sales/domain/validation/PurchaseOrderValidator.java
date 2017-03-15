package com.example.sales.domain.validation;

import com.example.common.domain.model.BusinessPeriod;
import com.example.common.domain.validation.BusinessPeriodValidator;
import com.example.inventory.domain.model.PlantReservation;
import com.example.sales.domain.model.POStatus;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

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

        if (order.getStatus() == POStatus.CLOSED || order.getStatus() == POStatus.OPEN) {
            if (order.getReservations()==null || order.getReservations().size()==0)
                errors.reject("reservations", "OPEN/CLOSED PO must have a valid plant reservation");
            else {
                PlantReservation reservation = order.getReservations().get(0);
                if (order.getRentalPeriod() != reservation.getSchedule())
                    errors.reject("reservations", "The period of the reservation must be consistent with the rental period");
            }

            if (order.getTotal().signum() < 0){
                errors.reject("total", "A PO must have a valid total cost");
            }
        }

//        A recently created PO must have a valid reference to a plant inventory entry, a valid rental period (e.g. start ⇐ end date, period must be in the future, and both dates must be different from null),
        errors.pushNestedPath("rentalPeriod");
        ValidationUtils.invokeValidator(periodValidator, order.getRentalPeriod(), errors);
        errors.popNestedPath();
    }
}