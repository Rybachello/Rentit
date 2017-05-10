package com.example.inventory.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import com.example.maintenance.domain.model.MaintenancePlan;
import com.example.maintenance.domain.model.MaintenanceTask;
import com.example.sales.domain.model.PurchaseOrder;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
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

    @OneToMany(mappedBy = "reservation")
    private List<MaintenanceTask> maintenance;

    public void update(PlantInventoryItem newPlant, BusinessPeriod newPeriod){
        this.plant = newPlant;
        this.schedule = newPeriod;
    }

    public void updatePeriod(BusinessPeriod newPeriod){
        this.schedule = newPeriod;
    }

    public static PlantReservation of(String id, BusinessPeriod rentalPeriod, PlantInventoryItem item, PurchaseOrder po) {
        return PlantReservation.of(id, rentalPeriod, item, po, null);
    }

}
