package com.example.maintenance.application.services;

import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.inventory.application.dto.PlantInventoryItemDTO;
import com.example.inventory.application.services.InventoryService;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Rybachello on 5/21/2017.
 */
@Service
public class MaintenanceService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    InventoryService inventoryService;

    public void replacePlantInventoryItem(PlantInventoryItemDTO dto) throws InvalidPurchaseOrderStatusException {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAllPurchaseOrdersByIdandPeriod(dto.get_id(), dto.getMaintenancePeriod().getStartDate(), dto.getMaintenancePeriod().getEndDate());

        for (PurchaseOrder po: purchaseOrders ) {
            try {
                 PurchaseOrder purchaseOrder =  inventoryService.createPlantReservation(po);
            }
            catch (PlantNotFoundException e)
            {
                po.closePurchaseOrder();
                purchaseOrderRepository.flush();
                //todo: need notify customer
                System.out.println("Need notify customer");
            }


        }

    }
}
