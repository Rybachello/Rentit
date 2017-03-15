package com.example.inventory.domain.model;

import com.example.maintenance.domain.model.MaintenanceTask;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lgarcia on 2/10/2017.
 */
@Entity
@Getter
@NoArgsConstructor(force=true, access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class PlantInventoryEntry {
    @Id
    String id;

    String name;

    String description;

    @Column(precision=8,scale=2)
    BigDecimal price;

    @OneToMany(mappedBy="plantInfo")
    private List<PlantInventoryItem> items;
}
