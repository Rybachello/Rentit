package com.example.sales.application.services;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.dto.PlantInventoryItemDTO;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.sales.application.dto.PlantDeliveryDTO;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Rybachello on 5/13/2017.
 */
@Service
public class PlantDeliveryAssembler extends ResourceAssemblerSupport<PurchaseOrder, PlantDeliveryDTO> {
    public PlantDeliveryAssembler() {
        super(PurchaseOrder.class, PlantDeliveryDTO.class);
    }

    @Override
    public PlantDeliveryDTO toResource(PurchaseOrder purchaseOrder) {

        PlantInventoryItem plantInventoryItem = purchaseOrder.getPlant();
        PlantInventoryItemDTO plantInventoryItemDTO = PlantInventoryItemDTO.of(plantInventoryItem.getId(),plantInventoryItem.getSerialNumber(),plantInventoryItem.getEquipmentCondition());

        BusinessPeriod businessPeriod = purchaseOrder.getRentalPeriod();
        BusinessPeriodDTO businessPeriodDTO = BusinessPeriodDTO.of(businessPeriod.getStartDate(), businessPeriod.getEndDate());

        //todo: contact details we need take from customer
        PlantDeliveryDTO plantDeliveryDTO = PlantDeliveryDTO.of(plantInventoryItemDTO, businessPeriodDTO, purchaseOrder.getConstructionSite(), null);

        return plantDeliveryDTO;
    }
}
