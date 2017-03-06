package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.AvaliablePlantReport;
import com.example.inventory.domain.model.PlantInventoryItem;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by stepan on 17/02/2017.
 */
public interface CustomInventoryRepository {
    List<AvaliablePlantReport> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate);

    List<PlantInventoryItem> findUnhired();

}
