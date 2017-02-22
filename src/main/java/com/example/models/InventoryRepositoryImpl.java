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

    public List<CorrectiveRepairsNumberPerYear> findCorrectiveRepairsNumberForLastFiveYears() {
       return em.createQuery("select NEW com.example.models.CorrectiveRepairsNumberPerYear(m.yearOfAction, count(task)) " +
                " from MaintenancePlan m JOIN m.tasks task" +
                " where m.yearOfAction >= ?1-4 and task.typeOfWork = com.example.models.TypeOfWork.CORRECTIVE" +
                " group by m.yearOfAction")
                .setParameter(1,LocalDate.now().getYear())
                .getResultList();
    }

    public List<CorrectiveRepairsCostPerYear> findCorrectiveRepairsCostsForLastFiveYears(){
        return em.createQuery("select NEW com.example.models.CorrectiveRepairsCostPerYear(m.yearOfAction, sum(task.price)) " +
                " from MaintenancePlan m JOIN m.tasks task" +
                " where m.yearOfAction >= ?1-4 and task.typeOfWork = com.example.models.TypeOfWork.CORRECTIVE" +
                " group by m.yearOfAction")
                .setParameter(1,LocalDate.now().getYear())
                .getResultList();
    }
}
