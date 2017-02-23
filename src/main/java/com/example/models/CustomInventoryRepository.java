package com.example.models;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by stepan on 17/02/2017.
 */
public interface CustomInventoryRepository {
    List<AvailablePlantReport> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate);

    List<PlantInventoryItem> findUnhired();

    List<CorrectiveRepairsNumberPerYear> findCorrectiveRepairsNumberForLastFiveYears();
    List<CorrectiveRepairsCostPerYear> findCorrectiveRepairsCostsForLastFiveYears();

}
