package com.example.inventory.application.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

/**
 * Created by Rybachello on 3/7/2017.
 */

@Data
@EqualsAndHashCode
public class PlantInventoryEntryDTO extends ResourceSupport {
    private String _id;
    private String name;
    private String description;
    private BigDecimal price;
}
