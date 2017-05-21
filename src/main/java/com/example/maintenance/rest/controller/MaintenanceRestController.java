package com.example.maintenance.rest.controller;

import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.inventory.application.dto.PlantInventoryItemDTO;
import com.example.maintenance.application.dto.MaintenanceDTO;
import com.example.maintenance.application.services.MaintenanceService;
import com.example.maintenance.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Rybachello on 5/21/2017.
 */
@RestController
@RequestMapping("api/inventory")
public class MaintenanceRestController {

    private String server = "localhost";
    private int port = 8091;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MaintenanceService maintenanceService;

    @PostMapping("/maintenance")
    @ResponseStatus(HttpStatus.OK)
    public String createMaintenance(@RequestBody MaintenanceDTO dto)
    {
        Boolean result =  restTemplate.postForObject(Constants.MAINTENANCE_URL+"/api/maintenances/tasks",dto,Boolean.class);
        HttpHeaders headers = new HttpHeaders();
        //todo: what to return
        return null;
    }

    @PostMapping("/maintenance/plants") //todo: rename?
    @ResponseStatus(HttpStatus.OK)
    public Boolean replacePlantInventoryItem(@RequestBody PlantInventoryItemDTO dto) throws InvalidPurchaseOrderStatusException {
        maintenanceService.replacePlantInventoryItem(dto);
        return true;
    }
}
