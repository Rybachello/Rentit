package com.example.inventory.application.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
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
    BusinessPeriodDTO maintenancePeriod;
    //String serialNumber;
    //EquipmentCondition equipmentCondition; //todo:do we need
}
