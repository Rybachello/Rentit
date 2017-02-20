package com.example.models;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Vasiliy on 2017.02.20.
 */
@Entity
@Data
public class MaintenancePlan {
    @Id @GeneratedValue
    Long id;

    Integer yearOfAction;

    @OneToMany(cascade = CascadeType.ALL)
    List<MaintenanceTask> tasks;

    @ManyToOne
    PlantInventoryItem inventoryItem;
}
