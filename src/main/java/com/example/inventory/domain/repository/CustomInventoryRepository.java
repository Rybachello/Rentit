package com.example.inventory.domain.repository;

import com.example.common.application.exceptions.InventoryEntryNotFoundException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.AvailablePlantReport;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by stepan on 17/02/2017.
 */
public interface CustomInventoryRepository {
    List<AvailablePlantReport> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate);
    List<PlantInventoryItem> findUnhired();
    List<PlantInventoryEntry> findInfoAvailablePlants(String name, LocalDate startDate, LocalDate endDate);
    List<PlantInventoryItem> findAvailablePlantItemsInBusinessPeriod(String entryId, BusinessPeriod period);
    List<PlantInventoryEntry> findAllPlants();
    PlantInventoryEntry getPlantEntryById(String entryId) throws InventoryEntryNotFoundException;

}
