package com.example.sales.domain.repository;

import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by stepan on 17/02/2017.
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {
}
