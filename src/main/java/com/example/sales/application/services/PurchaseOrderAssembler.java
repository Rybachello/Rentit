package com.example.sales.application.services;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by minhi_000 on 09.03.2017.
 */
@Service
public class PurchaseOrderAssembler extends ResourceAssemblerSupport<PurchaseOrder, PurchaseOrderDTO> {
    @Autowired
    PlantReservationRepository plantReservationRepository;

    public PurchaseOrderAssembler() {
        super(PurchaseOrder.class, PurchaseOrderDTO.class);
    }

    public PurchaseOrderDTO toResource(PlantInventoryEntry plant, BusinessPeriodDTO period, PurchaseOrder po){
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.set_id(plant.getId());
        dto.setName(plant.getName());
        dto.setTotal(po.getTotal());
        dto.setRentalPeriod(period);
        dto.setDescription(plant.getDescription());
        dto.setStatus(po.getStatus().toString());
        return dto;
    }

    @Override
    public PurchaseOrderDTO toResource(PurchaseOrder purchaseOrder) {
        PurchaseOrderDTO newDTO = new PurchaseOrderDTO();
        newDTO.setRentalPeriod(BusinessPeriodDTO.of(purchaseOrder.getRentalPeriod().getStartDate(), purchaseOrder.getRentalPeriod().getEndDate()));
        newDTO.setPlantId(plantReservationRepository.findByPurchaseOrderId(purchaseOrder.getId()).getId());
        return newDTO;
    }
}
