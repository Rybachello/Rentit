package com.example.inventory.application.services;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.application.services.BusinessPeriodDisassembler;
import com.example.common.domain.model.BusinessPeriod;
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
    InventoryRepositoryImpl repo;
    @Autowired
    PlantReservationRepository plantReservationRepository;
    //todo: add PlantInventoryEntryAssembler
    @Autowired
    BusinessPeriodDisassembler businessPeriodDisassembler;
    public List<PlantInventoryEntry> createListOfAvailablePlants(CatalogQueryDTO query) {
        return repo.findInfoAvailablePlants(query.getName(), query.getRentalPeriod().getStartDate(), query.getRentalPeriod().getStartDate());
    }

    public PlantReservation reserveItem(String plantId, BusinessPeriodDTO rentalPeriod,String poId) throws NoPlantAvailableException {
        //todo: refactor here covert businessDTO to
        BusinessPeriod businessPeriod = businessPeriodDisassembler.toResources(rentalPeriod);
        List<PlantInventoryItem> itemList = repo.findAvailablePlantItemsInBusinessPeriod(plantId, businessPeriod);
        if (itemList.size() < 1) {
            throw new NoPlantAvailableException();
        }
        PlantInventoryItem item = itemList.get(0);//find first

        PlantReservation pr = PlantReservation.of(IdentifierFactory.nextID(), businessPeriod, item.getId(), poId);
        plantReservationRepository.save(pr);
        return pr;
    }

    public static class NoPlantAvailableException extends Exception {

    }
}
