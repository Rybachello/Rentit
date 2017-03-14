package com.example.sales.domain.repository;

import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Vasiliy on 2017.03.14.
 */

public class PurchaseOrderRepositoryImpl {
    @Autowired
    EntityManager em;

    public List<PurchaseOrderDTO> findAllPO() {
        return em.createQuery(
                "select NEW com.example.sales.application.dto.PurchaseOrderDTO(po.id, e.name, po.status, r.schedule.startDate, r.schedule.endDate, e.price, e.description) " +
                        "FROM PurchaseOrder po, PlantReservation r, PlantInventoryItem i, PlantInventoryEntry e " +
                        "WHERE po.id = r.purchaseOrderId and r.plantId = i.id and i.plantInfoId = e.id")
                .getResultList();
    }

}
