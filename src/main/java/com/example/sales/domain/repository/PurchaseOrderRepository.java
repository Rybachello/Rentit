package com.example.sales.domain.repository;

import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by stepan on 17/02/2017.
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {
    @Query("select order from PurchaseOrder order where order.rentalPeriod.startDate = ?1 and order.status = 'OPEN'") //todo: how fix this? status = POStatus.OPEN
    List<PurchaseOrder> findAllPurchaseOrdersByStartDate(LocalDate date);
}
