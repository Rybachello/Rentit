package com.example.inventory.application;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by Rybachello on 3/7/2017.
 */

@Data
@EqualsAndHashCode
public class PlantInventoryEntryDTO {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
