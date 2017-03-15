package com.example.maintenance.domain.model;

import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantReservation;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Vasiliy on 2017.02.20.
 */
@Entity
@Getter
@NoArgsConstructor(force=true, access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class MaintenancePlan {
    @Id
    String id;

    Integer yearOfAction;

    @ManyToOne
    @JoinColumn(name = "inventoryItemId")
    PlantInventoryItem inventoryItem;

    @OneToMany(mappedBy="maintenancePlan")
    private List<MaintenanceTask> tasks;
}
