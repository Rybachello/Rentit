package com.example.inventory.domain.repository;


import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.*;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;


import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by stepan on 17/02/2017.
 */
public class InventoryRepositoryImpl implements CustomInventoryRepository {
    @Autowired
    EntityManager em;

    public List<AvailablePlantReport> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
        return em.createQuery("select NEW com.example.inventory.domain.model.AvailablePlantReport(e.name, e.description, COUNT(e.id)) " +
                "FROM PlantInventoryEntry e, PurchaseOrder r, PlantInventoryItem i " +
                "WHERE (e.name) LIKE ?1 AND (r.rentalPeriod.endDate < ?2 OR r.rentalPeriod.startDate > ?4) AND i.equipmentCondition = ?3 " +
                "GROUP BY e.name,e.description", AvailablePlantReport.class)
                .setParameter(1, "%"+name+"%")
                .setParameter(2, startDate)
                .setParameter(4, endDate)
                .setParameter(3, EquipmentCondition.SERVICEABLE)
                .getResultList();
    }

    public List<PlantInventoryEntry> findInfoAvailablePlants(String name, LocalDate startDate, LocalDate endDate){
        return em.createQuery("select distinct p FROM PlantInventoryEntry p, PlantInventoryItem i WHERE i.plantInfo.id = p.id and (p.name) LIKE ?1 AND i.equipmentCondition = ?4 AND i.id not in (select r.plant.id from PurchaseOrder r where r.rentalPeriod.startDate < ?3 and r.rentalPeriod.endDate > ?2)")
                .setParameter(1,"%"+name+"%")
                .setParameter(2, startDate)
                .setParameter(3, endDate)
                .setParameter(4, EquipmentCondition.SERVICEABLE)
                .getResultList();
    }

    public List<PlantInventoryItem> findUnhired() {
        return em.createQuery(
                "select distinct p from PlantInventoryItem p, PurchaseOrder r where (r.plant.id = p.id and r.rentalPeriod.startDate < sysdate-30*6 and r.rentalPeriod.endDate < sysdate-30*6) or (p.id not in (select q.plant.id from PurchaseOrder q))")
                .getResultList();
    }
    public boolean isAPlantAvailableStrict(PlantInventoryEntry entry, BusinessPeriod period) {
        Long count = (Long) em.createQuery(
                "select count(pi) from PlantInventoryItem pi where (pi.plantInfo.id = ?1 and pi.equipmentCondition = com.example.inventory.domain.model.EquipmentCondition.SERVICEABLE and pi.id not in (select pr.plant.id from PurchaseOrder pr where pr.rentalPeriod.startDate < ?3 and pr.rentalPeriod.endDate > ?2))")
                .setParameter(1, entry.getId())
                .setParameter(2, period.getStartDate())
                .setParameter(3, period.getEndDate())
                .getSingleResult();

        return count > 0;
    }

    @Override
    public List<PlantInventoryItem> findAvailablePlantItemsInBusinessPeriod(String entryId, BusinessPeriod period) {
        //noinspection unchecked
        return em.createQuery(
                "select p from PlantInventoryItem p where p.plantInfo.id = ?1 and " +
                        "p.equipmentCondition = com.example.inventory.domain.model.EquipmentCondition.SERVICEABLE and p.id not in " +
                        "(select r.plant.id from PurchaseOrder r where ?2 < r.rentalPeriod.endDate and ?3 > r.rentalPeriod.startDate)")
                .setParameter(1, entryId)
                .setParameter(2, period.getStartDate())
                .setParameter(3, period.getEndDate())
                .getResultList();
    }

    public List<PlantInventoryEntry> findAllPlants() {
        return em.createQuery("select distinct p FROM PlantInventoryEntry p, PlantInventoryItem i WHERE i.equipmentCondition = ?1")
                .setParameter(1, EquipmentCondition.SERVICEABLE)
                .getResultList();
    }

    public PlantInventoryEntry getPlantEntryById(String entryId) {
        return (PlantInventoryEntry)em.createQuery("select p FROM PlantInventoryEntry p WHERE p.id = ?1")
                .setParameter(1, entryId)
                .getSingleResult();
    }
}
