package com.example.sales.application.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.inventory.application.dto.PlantInventoryItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Rybachello on 5/13/2017.
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
public class PlantDeliveryDTO extends ResourceSupport {
    PlantInventoryItemDTO plantItem;
    BusinessPeriodDTO businessPeriod;
    String constructionSite;
    String contactDetails;

}
