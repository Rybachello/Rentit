package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.PlantInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by stepan on 17/02/2017.
 */
@Repository
public interface PlantInventoryItemRepository extends JpaRepository<PlantInventoryItem, String> {
}
