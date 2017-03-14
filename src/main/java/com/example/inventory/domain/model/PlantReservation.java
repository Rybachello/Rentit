package com.example.inventory.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import com.example.maintenance.domain.model.MaintenancePlan;
import com.example.sales.domain.model.PurchaseOrder;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(force=true, access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class PlantReservation {
    @Id
    String id;

    @Embedded
    BusinessPeriod schedule;

    @ManyToOne
    @JoinColumn(name = "plantId")
    PlantInventoryItem plant;// PlantInventoryItem id

    @ManyToOne
    @JoinColumn(name = "purchaseOrderId")
    PurchaseOrder purchaseOrder;
}
