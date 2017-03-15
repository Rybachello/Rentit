package com.example.sales.application.services;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by minhi_000 on 09.03.2017.
 */
@Service
public class PurchaseOrderAssembler extends ResourceAssemblerSupport<PurchaseOrder, PurchaseOrderDTO> {
    @Autowired
    PlantReservationRepository plantReservationRepository;
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PurchaseOrderAssembler() {
        super(PurchaseOrder.class, PurchaseOrderDTO.class);
    }

    @Override
    public PurchaseOrderDTO toResource(PurchaseOrder purchaseOrder) {
        PurchaseOrderDTO newDTO = createResourceWithId(purchaseOrder.getId(), purchaseOrder);
        newDTO.set_id(purchaseOrder.getId());

        LocalDate startDate = purchaseOrder.getRentalPeriod().getStartDate();
        LocalDate endDate = purchaseOrder.getRentalPeriod().getEndDate();
        BusinessPeriodDTO businessPeriod = BusinessPeriodDTO.of(startDate, endDate);

        PlantInventoryEntry plant = purchaseOrder.getPlant();

        newDTO.setStatus(purchaseOrder.getStatus());
        newDTO.setTotal(purchaseOrder.getTotal());

        newDTO.setRentalPeriod(businessPeriod);
        newDTO.setPlant(plantInventoryEntryAssembler.toResource(plant));
        return newDTO;
    }
}
