package com.example.common.application.exceptions;

/**
 * Created by stepan on 21/03/2017.
 */
public class InvalidPurchaseOrderStatusException extends Throwable {
    public InvalidPurchaseOrderStatusException(String msg) {
        super(msg);
    }
}
