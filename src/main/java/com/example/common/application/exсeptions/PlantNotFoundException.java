package com.example.common.application.exсeptions;

/**
 * Created by Rybachello on 3/11/2017.
 */
public class PlantNotFoundException extends Throwable {
    public PlantNotFoundException(String msg) {
        super(msg);
    }
}