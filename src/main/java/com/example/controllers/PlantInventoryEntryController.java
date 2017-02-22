package com.example.controllers;

import com.example.models.PlantInventoryEntry;
import com.example.models.PlantInventoryEntryRepository;
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
    PlantInventoryEntryRepository repo;
    
    @GetMapping("/plants")
    public List<PlantInventoryEntry> findAll() {
        return repo.findByNameContaining("ex");
    }
}
