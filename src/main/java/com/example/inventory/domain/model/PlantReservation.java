package com.example.inventory.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import com.example.maintenance.domain.model.MaintenancePlan;
import com.example.sales.domain.model.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PlantReservation {
    @Id
    String id;

    @Embedded
    BusinessPeriod schedule;

    String plantId;

    String purchaseOrderId;
}
