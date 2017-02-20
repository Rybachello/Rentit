package com.example.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by stepan on 17/02/2017.
 */
@Repository
public interface PlantReservationRepository extends JpaRepository<PlantReservation, Long> {
}
