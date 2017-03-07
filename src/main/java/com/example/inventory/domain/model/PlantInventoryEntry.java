package com.example.inventory.domain.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by lgarcia on 2/10/2017.
 */
@Entity
@Data
public class PlantInventoryEntry {
    @Id
    String id;

    String name;

    String description;

    @Column(precision=8,scale=2)
    BigDecimal price;
}
