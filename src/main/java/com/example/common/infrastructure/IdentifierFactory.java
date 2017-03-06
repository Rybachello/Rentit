package com.example.common.infrastructure;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by stepan on 06/03/2017.
 */
@Service
public class IdentifierFactory {
    public static String nextID() {
        return UUID.randomUUID().toString();
    }
}
