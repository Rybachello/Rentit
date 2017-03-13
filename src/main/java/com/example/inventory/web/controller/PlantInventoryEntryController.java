package com.example.inventory.web.controller;

import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lgarcia on 2/10/2017.
 */
@RestController
public class PlantInventoryEntryController {
    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;
    
    @GetMapping("/plants")
    public List<PlantInventoryEntry> findAll() {
        return plantInventoryEntryRepository.findByNameContaining("ex");
    }
}
