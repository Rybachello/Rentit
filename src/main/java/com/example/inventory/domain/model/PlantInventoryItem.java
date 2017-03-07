package com.example.inventory.domain.model;

import com.example.inventory.domain.model.EquipmentCondition;
import com.example.inventory.domain.model.PlantInventoryEntry;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PlantInventoryItem {
    @Id
    String id;

    String serialNumber;

    @Enumerated(EnumType.STRING)
    EquipmentCondition equipmentCondition;

    String plantInfoId;
}
