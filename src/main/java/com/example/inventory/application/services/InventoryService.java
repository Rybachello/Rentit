package com.example.inventory.application.services;

import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.common.application.exceptions.PlantNotAvailableException;
import com.example.common.application.services.BusinessPeriodDisassembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.repository.CustomInventoryRepository;
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

    public PurchaseOrder createPlantReservation(String plantId, BusinessPeriod rentalPeriod, PurchaseOrder po) throws PlantNotAvailableException {
        List<PlantInventoryItem> itemList = inventoryRepository.findAvailablePlantItemsInBusinessPeriod(plantId, rentalPeriod);
        if (itemList.size() == 0) {
            throw new PlantNotAvailableException("Requested plant is unavailable");
        }
        //find first
        PlantInventoryItem item = itemList.get(0);
        //create new plantReservation
        po.confirm(rentalPeriod, item);
        return po;
    }

    public PurchaseOrder updatePlantReservation(PurchaseOrder po, BusinessPeriod newPeriod) throws PlantNotAvailableException, InvalidPurchaseOrderStatusException {
        // if we need to extend the period and therefore keep the same plant item
        if (po.getRentalPeriod().intersects(newPeriod)) {
            boolean ok = true;

            // if new period starts earlier
            if (newPeriod.getStartDate().isBefore(po.getRentalPeriod().getStartDate())) {
                List<PlantInventoryItem> itemList = inventoryRepository.findAvailablePlantItemsInBusinessPeriod(
                        po.getPlant().getId(),
                        BusinessPeriod.of(newPeriod.getStartDate(), po.getRentalPeriod().getStartDate()));
                ok = ok && itemList.stream().filter(i -> i.getId() == po.getPlant().getId()).count() > 0;
            }

            // if new period ends later
            if (newPeriod.getEndDate().isAfter(po.getRentalPeriod().getEndDate())) {
                List<PlantInventoryItem> itemList = inventoryRepository.findAvailablePlantItemsInBusinessPeriod(
                        po.getPlant().getId(),
                        BusinessPeriod.of(po.getRentalPeriod().getEndDate(), newPeriod.getEndDate()));
                ok = ok && itemList.stream().filter(i -> i.getId() == po.getPlant().getId()).count() > 0;
            }

            if (ok){
                po.updateRentalPeriod(newPeriod);
            }

        } // else we can give the same entry, but another item; as dates do not intersect we can just search for available plants.
        else {
            List<PlantInventoryItem> itemList = inventoryRepository.findAvailablePlantItemsInBusinessPeriod(
                    po.getPlant().getId(), newPeriod);
            if (itemList.size() == 0) {
                throw new PlantNotAvailableException("Requested plant is unavailable");
            } else {
                po.confirm(newPeriod, itemList.get(0));
            }
        }

        return po;
    }

    public List<PlantInventoryEntryDTO> allPlants() {
        return plantInventoryEntryAssembler.toResources(inventoryRepository.findAllPlants());
    }

    public PlantInventoryEntryDTO getEntryById(String entryId) {
        return plantInventoryEntryAssembler.toResource(inventoryRepository.getPlantEntryById(entryId));
    }

}
