package com.example.sales.domain.repository;

import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Vasiliy on 2017.03.14.
 */

public class PurchaseOrderRepositoryImpl implements CustomPurchaseOrderRepository {
    @Autowired
    EntityManager em;

    public List<PurchaseOrderDTO> findAllPO() {
        return em.createQuery(
                "select NEW com.example.sales.application.dto.PurchaseOrderDTO(po.id, e.name, po.status, r.schedule.startDate, r.schedule.endDate, e.price, e.description) " +
                        "FROM PurchaseOrder po, PlantReservation r, PlantInventoryItem i, PlantInventoryEntry e " +
                        "WHERE po.id = r.purchaseOrder.id and r.plant.id = i.id and i.plantInfo.id = e.id")
                .getResultList();
    }

    @Override
    public PurchaseOrderDTO findPurchaseOrderById(String id) {
        return (PurchaseOrderDTO) em.createQuery(
                "select NEW com.example.sales.application.dto.PurchaseOrderDTO(po.id, e.name, po.status, r.schedule.startDate, r.schedule.endDate, e.price, e.description) " +
                        "FROM PurchaseOrder po, PlantReservation r, PlantInventoryItem i, PlantInventoryEntry e " +
                        "WHERE po.id = r.purchaseOrder.id and r.plant.id = i.id and i.plantInfo.id = e.id and po.id = ?1")
                .setParameter(1,id)
                .getSingleResult();
    }

}
