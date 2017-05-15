package com.example.inventory.application.dto;

import com.example.inventory.domain.model.EquipmentCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Rybachello on 5/14/2017.
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
public class PlantInventoryItemDTO extends ResourceSupport {

    String _id;
    String serialNumber;
    EquipmentCondition equipmentCondition;
}
