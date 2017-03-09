package com.example.inventory.domain.repository;


import com.example.common.application.BusinessPeriodDTO;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.AvailablePlantReport;
import com.example.inventory.domain.model.EquipmentCondition;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.maintenance.domain.model.CorrectiveRepairsCostPerYear;
import com.example.maintenance.domain.model.CorrectiveRepairsNumberPerYear;
import com.example.maintenance.domain.model.TypeOfWork;
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
                "FROM PlantInventoryEntry e, PlantReservation r, PlantInventoryItem i " +
                "WHERE (e.name) LIKE ?1 AND r.schedule.endDate = ?2 AND i.equipmentCondition = ?3 " +
                "GROUP BY e.name,e.description", AvailablePlantReport.class)
                .setParameter(1, "%"+name+"%")
                .setParameter(2, startDate)
                .setParameter(3, EquipmentCondition.SERVICEABLE)
                .getResultList();
    }

    public List<PlantInventoryEntry> findInfoAvailablePlants(String name, LocalDate startDate, LocalDate endDate){
        return em.createQuery("select distinct p FROM PlantInventoryEntry p, PlantReservation r, PlantInventoryItem i WHERE (p.name) LIKE ?1 AND i.equipmentCondition = ?4 AND (r.schedule.startDate > ?3 OR r.schedule.endDate < ?2)")
                .setParameter(1,"%"+name+"%")
                .setParameter(2, startDate)
                .setParameter(3, endDate)
                .setParameter(4, EquipmentCondition.SERVICEABLE)
                .getResultList();
    }

    public List<PlantInventoryItem> findUnhired() {
        return em.createQuery(
                "select distinct p from PlantInventoryItem p, PlantReservation r where (r.plantId = p.id and r.schedule.startDate < sysdate-30*6 and r.schedule.endDate < sysdate-30*6) or (p.id not in (select q.plantId from PlantReservation q))")
                .getResultList();
    }
    public boolean isAPlantAvailableStrict(PlantInventoryEntry entry, BusinessPeriod period) {
        Long count = (Long) em.createQuery(
                "select count(pi) from PlantInventoryItem pi where (pi.plantInfoId = ?1 and pi.equipmentCondition = com.example.inventory.domain.model.EquipmentCondition.SERVICEABLE and pi.id not in (select plantId from PlantReservation pr where pr.schedule.startDate < ?3 and pr.schedule.endDate > ?2))")
                .setParameter(1, entry.getId())
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
                "select count(pi) from PlantInventoryItem pi where (pi.plantInfoId = ?1 and pi.id not in "+
                            "(select pr.plantId from PlantReservation pr where (pr.schedule.startDate < ?3 and "+
                            "pr.schedule.endDate > ?2)) and pi.id in "+
                                    "(select pr2.plantId from MaintenanceTask mt, PlantReservation pr2 where pr2.id = mt.reservationId and "+
                                    "pr2.schedule.endDate < ?4 and pr2.schedule.endDate > ?5))")
                .setParameter(1, entry.getId())
                .setParameter(2, period.getStartDate())
                .setParameter(3, period.getEndDate())
                .setParameter(4, period.getStartDate().minusWeeks(1))
                .setParameter(5, LocalDate.now())
                .getSingleResult();

        return count > 0;
    }
    public List<CorrectiveRepairsNumberPerYear> findCorrectiveRepairsNumberForLastFiveYears() {
        return em.createQuery("select NEW com.example.maintenance.domain.model.CorrectiveRepairsNumberPerYear(m.yearOfAction, count(task)) " +
                " from MaintenancePlan m, MaintenanceTask task" +
                " where task.maintenancePlanId = m.id and m.yearOfAction >= ?1-4 and task.typeOfWork = ?2" +
                " group by m.yearOfAction")
                .setParameter(1,LocalDate.now().getYear())
                .setParameter(2, com.example.maintenance.domain.model.TypeOfWork.CORRECTIVE)
                .getResultList();
    }

    public List<CorrectiveRepairsCostPerYear> findCorrectiveRepairsCostsForLastFiveYears(){
        return em.createQuery("select NEW com.example.maintenance.domain.model.CorrectiveRepairsCostPerYear(m.yearOfAction, sum(task.price)) " +
                " from MaintenancePlan m, MaintenanceTask task" +
                " where m.id = task.maintenancePlanId and m.yearOfAction >= ?1-4 and task.typeOfWork = com.example.maintenance.domain.model.TypeOfWork.CORRECTIVE" +
                " group by m.yearOfAction")
                .setParameter(1,LocalDate.now().getYear())
                .getResultList();
    }
    @Override
    public List<PlantInventoryItem> findAvailablePlantItemsInBusinessPeriod(String entryId, BusinessPeriod period) {
        //noinspection unchecked
        return em.createQuery(
                "select p from PlantInventoryItem p where p.plantInfoId = ?1 and " +
                        "p.equipmentCondition = com.example.inventory.domain.model.EquipmentCondition.SERVICEABLE and p.id not in " +
                        "(select r.plantId from PlantReservation r where ?2 < r.schedule.endDate and ?3 > r.schedule.startDate)")
                .setParameter(1, entryId)
                .setParameter(2, period.getStartDate())
                .setParameter(3, period.getEndDate())
                .getResultList();
    }
}
