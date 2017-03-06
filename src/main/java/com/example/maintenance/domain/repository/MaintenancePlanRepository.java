package com.example.maintenance.domain.repository;

import com.example.maintenance.domain.model.MaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by stepan on 17/02/2017.
 */
@Repository
public interface MaintenancePlanRepository extends JpaRepository<MaintenancePlan, String> {
}
