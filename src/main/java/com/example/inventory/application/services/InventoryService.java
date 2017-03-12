package com.example.inventory.application.services;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.application.exсeptions.PlantNotFoundException;
import com.example.common.application.services.BusinessPeriodDisassembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.infrastructure.IdentifierFactory;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.InventoryRepositoryImpl;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.sales.domain.web.dto.CatalogQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Rybachello on 3/7/2017.
 */
@Service
public class InventoryService {
    @Autowired
    InventoryRepositoryImpl inventoryRepository;
    @Autowired
    PlantReservationRepository plantReservationRepository;
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;
    @Autowired
    BusinessPeriodDisassembler businessPeriodDisassembler;

    public List<PlantInventoryEntryDTO> createListOfAvailablePlants(CatalogQueryDTO query) {
        return plantInventoryEntryAssembler.toResources(inventoryRepository.findInfoAvailablePlants(query.getName(), query.getRentalPeriod().getStartDate(), query.getRentalPeriod().getStartDate()));
    }
    //todo: remove poID here
    public PlantReservation createPlantReservation(String plantId, BusinessPeriod  rentalPeriod, String poId) throws PlantNotFoundException {
        List<PlantInventoryItem> itemList = inventoryRepository.findAvailablePlantItemsInBusinessPeriod(plantId, rentalPeriod);
        if (itemList.size() == 0) {
            throw new PlantNotFoundException("Requested plant is unavailable") ;
        }
        PlantInventoryItem item = itemList.get(0);//find first
        //create new plantReservation
        PlantReservation plantReservation = PlantReservation.of(IdentifierFactory.nextID(), rentalPeriod, item.getId(), poId);
        //save to the database
        plantReservationRepository.save(plantReservation);
        return plantReservation;
    }

}
