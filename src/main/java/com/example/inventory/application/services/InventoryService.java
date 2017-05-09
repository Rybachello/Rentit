package com.example.inventory.application.services;

import com.example.common.application.exceptions.PlantNotAvailableException;
import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.application.services.BusinessPeriodDisassembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.domain.repository.CustomInventoryRepository;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.inventory.infrastructure.IdentifierFactory;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.InventoryRepositoryImpl;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.sales.domain.model.PurchaseOrder;
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
    CustomInventoryRepository inventoryRepository;
    @Autowired
    PlantReservationRepository plantReservationRepository;
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;
    @Autowired
    BusinessPeriodDisassembler businessPeriodDisassembler;

    public List<PlantInventoryEntryDTO> createListOfAvailablePlants(CatalogQueryDTO query) {
        return plantInventoryEntryAssembler.toResources(
                inventoryRepository.findInfoAvailablePlants(
                        query.getName(),
                        query.getRentalPeriod().getStartDate(),
                        query.getRentalPeriod().getStartDate()));
    }

    public PlantReservation createPlantReservation(String plantId, BusinessPeriod  rentalPeriod, PurchaseOrder po) throws PlantNotAvailableException {
        List<PlantInventoryItem> itemList = inventoryRepository.findAvailablePlantItemsInBusinessPeriod(plantId, rentalPeriod);
        if (itemList.size() == 0) {
            throw new PlantNotAvailableException("Requested plant is unavailable") ;
        }
        //find first
        PlantInventoryItem item = itemList.get(0);
        //create new plantReservation
        PlantReservation plantReservation = PlantReservation.of(IdentifierFactory.nextID(), rentalPeriod, item, po);
        return plantReservation;
    }

    public List<PlantInventoryEntryDTO> allPlants() {
        return plantInventoryEntryAssembler.toResources(inventoryRepository.findAllPlants());
    }

    public PlantInventoryEntryDTO getEntryById(String entryId) {
        return plantInventoryEntryAssembler.toResource(inventoryRepository.getPlantEntryById(entryId));
    }

}
