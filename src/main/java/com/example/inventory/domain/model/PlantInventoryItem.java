package com.example.inventory.domain.model;

import com.example.inventory.domain.model.EquipmentCondition;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.maintenance.domain.model.MaintenancePlan;
import com.example.maintenance.domain.model.MaintenanceTask;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

//    @OneToMany(mappedBy="plant")
//    private List<PlantReservation> reservations;

//    @OneToMany(mappedBy="inventoryItem")
//    private List<MaintenancePlan> maintenancePlans;

    public void setEquipmentCondition(EquipmentCondition equipmentCondition) {
        this.equipmentCondition = equipmentCondition;
    }
}
