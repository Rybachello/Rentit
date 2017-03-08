package com.example.inventory.application.services;

import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.repository.InventoryRepositoryImpl;
import com.example.sales.domain.web.controller.dto.CatalogQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Rybachello on 3/7/2017.
 */
@Service
public class InventoryService {


    @Autowired
    InventoryRepositoryImpl repo;
    public List<PlantInventoryEntry> createListOfAvailablePlants(CatalogQueryDTO query){
        return repo.findInfoAvailablePlants(query.getName(),query.getRentalPeriod().getStartDate(),query.getRentalPeriod().getStartDate());
    }
}
