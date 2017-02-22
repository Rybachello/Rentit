package com.example.models;

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

    public List<AvaliablePlantReport> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
        return em.createQuery("select NEW com.example.models.AvaliablePlantReport(e.name, e.description, COUNT(e.id)) " +
                "FROM PlantInventoryEntry e, PlantReservation r, PlantInventoryItem i " +
                "WHERE (e.name) LIKE ?1 AND r.schedule.endDate = ?2 AND i.equipmentCondition = ?3 " +
                "GROUP BY e.name,e.description", AvaliablePlantReport.class)
                .setParameter(1, "%"+name+"%")
                .setParameter(2, startDate)
                .setParameter(3, EquipmentCondition.SERVICEABLE)
                .getResultList();
    }

    public List<PlantInventoryItem> findUnhired() {
        return em.createQuery(
                "select distinct p from PlantInventoryItem p, PlantReservation r where (r.plant.id = p.id and r.schedule.startDate < sysdate-30*6 and r.schedule.endDate < sysdate-30*6) or (p.id not in (select q.plant.id from PlantReservation q))")
                .getResultList();
    }

    public boolean isAPlantAvailableStrict(PlantInventoryEntry entry, BusinessPeriod period) {
        Long count = (Long) em.createQuery(
                "select count(pi) from PlantInventoryItem pi where (pi.plantInfo.id = ?1 and pi.equipmentCondition = com.example.models.EquipmentCondition.SERVICEABLE and pi.id not in (select pr.plant.id from PlantReservation pr where pr.schedule.startDate < ?3 and pr.schedule.endDate > ?2))")
                .setParameter(1, entry.id)
                .setParameter(2, period.getStartDate())
                .setParameter(3, period.getEndDate())
                .getSingleResult();

        return count > 0;
    }

    public boolean isAPlantAvailableRelaxed(PlantInventoryEntry entry, BusinessPeriod period) {
        boolean b = isAPlantAvailableStrict(entry, period);
        if (b)
            return b;

        if (LocalDate.now().isAfter(period.getStartDate().minusWeeks(3)))
            return false;

        long count = (Long) em.createQuery(
                "select count(pi) from PlantInventoryItem pi where (pi.plantInfo.id = ?1 and pi.id not in (select pr.plant.id from PlantReservation pr where (pr.schedule.startDate < ?3 and pr.schedule.endDate > ?2)) and pi.id in (select mt.reservation.plant.id from MaintenanceTask mt where mt.reservation.schedule.endDate < ?4 and mt.reservation.schedule.endDate > ?5))")
                .setParameter(1, entry.id)
                .setParameter(2, period.getStartDate())
                .setParameter(3, period.getEndDate())
                .setParameter(4, period.getStartDate().minusWeeks(1))
                .setParameter(5, LocalDate.now())
                .getSingleResult();

        return count > 0;
    }
}
