package com.example.models;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by stepan on 17/02/2017.
 */
public class InventoryRepositoryImpl {
    @Autowired
    EntityManager em;

    public List<PlantInventoryEntry> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
        // BLAH BLAH IN THE PRINTOUT
    }
}
