package com.example.maintenance.domain.repository;

import com.example.maintenance.domain.model.MaintenanceTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by stepan on 17/02/2017.
 */
@Repository
public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, String> {
}
