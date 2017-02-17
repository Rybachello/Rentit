package com.example.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lgarcia on 2/10/2017.
 */
@Repository
public interface PlantInventoryEntryRepository extends JpaRepository<PlantInventoryEntry, Long> {
}
