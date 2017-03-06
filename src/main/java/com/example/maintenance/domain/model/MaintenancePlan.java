package com.example.maintenance.domain.model;

import com.example.inventory.domain.model.PlantInventoryItem;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Vasiliy on 2017.02.20.
 */
@Entity
@Data
public class MaintenancePlan {
    @Id
    String id;

    Integer yearOfAction;

    @OneToMany(cascade = CascadeType.ALL)
    List<MaintenanceTask> tasks;

    @ManyToOne
    PlantInventoryItem inventoryItem;
}
