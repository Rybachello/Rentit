package com.example.maintenance.rest.controller;

import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.inventory.application.dto.PlantInventoryItemDTO;
import com.example.maintenance.application.dto.MaintenanceDTO;
import com.example.maintenance.application.services.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Rybachello on 5/21/2017.
 */
@RestController
@RequestMapping("api/inventory")
public class MaintenanceRestController {

    private String server = "localhost";
    private int port = 8091;


    @Autowired
    MaintenanceService maintenanceService;

    @PostMapping("/maintenance")
    @ResponseStatus(HttpStatus.OK)
    public String createMaintenance(@RequestBody MaintenanceDTO dto)
    {
        maintenanceService.createMaintenance(dto);
        return null;
    }

    @PostMapping("/maintenance/plants")
    @ResponseStatus(HttpStatus.OK)
    public Boolean replacePlantInventoryItem(@RequestBody PlantInventoryItemDTO dto) throws InvalidPurchaseOrderStatusException {
        maintenanceService.replacePlantInventoryItem(dto);
        return true;
    }
}
