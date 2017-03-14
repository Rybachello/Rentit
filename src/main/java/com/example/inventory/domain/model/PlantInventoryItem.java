package com.example.inventory.domain.model;

import com.example.inventory.domain.model.EquipmentCondition;
import com.example.inventory.domain.model.PlantInventoryEntry;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(force=true, access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class PlantInventoryItem {
    @Id
    String id;

    String serialNumber;

    @Enumerated(EnumType.STRING)
    EquipmentCondition equipmentCondition;

    @ManyToOne
    @JoinColumn(name = "plantInfoId")
    PlantInventoryEntry plantInfo; // PlantInventoryEntry id

    public void setEquipmentCondition(EquipmentCondition equipmentCondition) {
        this.equipmentCondition = equipmentCondition;
    }
}
