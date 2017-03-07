package com.example.models;

import com.example.DemoApplication;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.inventory.domain.model.EquipmentCondition;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.*;
import com.example.maintenance.domain.model.MaintenanceTask;
import com.example.maintenance.domain.repository.MaintenancePlanRepository;
import com.example.maintenance.domain.repository.MaintenanceTaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@Sql(scripts="plants-dataset.sql")
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InventoryRepositoryTests {
    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepo;
    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepo;
    @Autowired
    PlantReservationRepository plantReservationRepo;
    @Autowired
    InventoryRepository inventoryRepo;
    @Autowired
    InventoryRepositoryImpl inventoryRepoImpl;
    @Autowired
    MaintenanceTaskRepository maintenanceTaskRepo;
    @Autowired
    MaintenancePlanRepository maintenancePlanRepo;

    @Test
    public void queryUnhiredPlants() {
        assertThat(inventoryRepo.findUnhired().size()).isEqualTo(3);
    }

    @Test
    public void checkIfPlantAvailableStrict()
    {
        boolean a = inventoryRepoImpl.isAPlantAvailableStrict(
                plantInventoryEntryRepo.findOne("1"),
                            BusinessPeriod.of(LocalDate.of(2012, 12, 12), LocalDate.of(2012, 12, 13)));
        assert(a);
    }

    private void addReservationAndCheckAvailability(BusinessPeriod of) {
        PlantReservation reserve = PlantReservation.of(IdentifierFactory.nextID(), of, "1", null);
        //reserve.setSchedule(of);
//      //reserve.setPlant(plantInventoryItemRepo.findOne("1"));
        plantReservationRepo.save(reserve);
    }

    private boolean IsAPlantAvailableStrict() {
        return inventoryRepoImpl.isAPlantAvailableStrict(
                plantInventoryEntryRepo.findOne("1"),
                BusinessPeriod.of(LocalDate.of(2012, 12, 12), LocalDate.of(2012, 12, 22)));
    }

    private boolean IsAPlantAvailableRelaxed() {
        return inventoryRepoImpl.isAPlantAvailableRelaxed(
                plantInventoryEntryRepo.findOne("1"),
                BusinessPeriod.of(LocalDate.of(2012, 12, 12), LocalDate.of(2012, 12, 22)));
    }

    @Test
    public void checkIfPlantAvailableDateNearReservation()
    {
        addReservationAndCheckAvailability(BusinessPeriod.of(LocalDate.of(2012, 12, 10), LocalDate.of(2012, 12, 12)));
        assert(IsAPlantAvailableStrict());
        assert(IsAPlantAvailableRelaxed());
    }

    @Test
    public void checkIfPlantUnavailableStartDateIntersectsWithReservation()
    {
        addReservationAndCheckAvailability(BusinessPeriod.of(LocalDate.of(2012, 12, 10), LocalDate.of(2012, 12, 13)));
        assert(!IsAPlantAvailableStrict());
        assert(!IsAPlantAvailableRelaxed());
    }

    @Test
    public void checkIfPlantUnavailableEndDateIntersectsWithReservation()
    {
        addReservationAndCheckAvailability(BusinessPeriod.of(LocalDate.of(2012, 12, 16), LocalDate.of(2012, 12, 25)));
        assert(!IsAPlantAvailableStrict());
        assert(!IsAPlantAvailableRelaxed());
    }

    @Test
    public void checkIfPlantUnavailableDatesWithinReservation()
    {
        addReservationAndCheckAvailability(BusinessPeriod.of(LocalDate.of(2012, 12, 10), LocalDate.of(2012, 12, 25)));
        assert(!IsAPlantAvailableStrict());
        assert(!IsAPlantAvailableRelaxed());
    }

    @Test
    public void checkIfPlantUnavailableEndDatesOverReservation()
    {
        addReservationAndCheckAvailability(BusinessPeriod.of(LocalDate.of(2012, 12, 13), LocalDate.of(2012, 12, 16)));
        assert(!IsAPlantAvailableStrict());
        assert(!IsAPlantAvailableRelaxed());
    }

    @Test
    public void checkIfPlantAvailableRelaxed()
    {
        boolean a = inventoryRepoImpl.isAPlantAvailableRelaxed(
                plantInventoryEntryRepo.findOne("1"),
                BusinessPeriod.of(LocalDate.of(2012, 12, 12), LocalDate.of(2012, 12, 13)));
        assert(a);
    }

    @Test
    public void checkIfPlantAvailableUnserviceable()
    {
        PlantInventoryItem item = plantInventoryItemRepo.findOne("1");
        item.setEquipmentCondition(EquipmentCondition.UNSERVICEABLEREPAIRABLE);
        plantInventoryItemRepo.save(item);
        assert(!IsAPlantAvailableStrict());
        assert(!IsAPlantAvailableRelaxed());
    }

    private PlantReservation addReservation(String itemId, BusinessPeriod period) {
        PlantReservation reservation = PlantReservation.of(IdentifierFactory.nextID(), period, itemId, null);
        plantReservationRepo.save(reservation);
        return reservation;
    }

    @Test
    public void checkIfPlantAvailableUnserviceableRelaxed()
    {
        LocalDate weekFromNow = LocalDate.now().plusWeeks(1);
        LocalDate tenDaysFromNow = LocalDate.now().plusDays(10);
        LocalDate monthFromNow = LocalDate.now().plusMonths(1);
        LocalDate twoMonthFromNow = LocalDate.now().plusMonths(2);

        maintenancePlanRepo.deleteAll();
        maintenanceTaskRepo.deleteAll();
        plantReservationRepo.deleteAll();

        PlantInventoryItem item = plantInventoryItemRepo.findOne("1");
        item.setEquipmentCondition(EquipmentCondition.UNSERVICEABLEREPAIRABLE);
        plantInventoryItemRepo.save(item);

        MaintenanceTask mt = new MaintenanceTask();
        PlantReservation pr = PlantReservation.of(
                IdentifierFactory.nextID(),
                BusinessPeriod.of(weekFromNow, tenDaysFromNow),
                item.getId(),
                null);
        plantReservationRepo.save(pr);

        mt.setId(IdentifierFactory.nextID());
        mt.setReservationId(pr.getId());
        maintenanceTaskRepo.save(mt);

        boolean a = inventoryRepoImpl.isAPlantAvailableRelaxed(
                plantInventoryEntryRepo.findOne("1"),
                BusinessPeriod.of(monthFromNow, twoMonthFromNow));
        assert (a);
    }

    @Test
    public void checkIfPlantUnavailableUnserviceableRelaxedLateMaintainance()
    {
        LocalDate weekFromNow = LocalDate.now().plusWeeks(1);
        LocalDate almostMonthFromNow = LocalDate.now().plusDays(27);
        LocalDate monthFromNow = LocalDate.now().plusMonths(1);
        LocalDate twoMonthFromNow = LocalDate.now().plusMonths(2);

        maintenancePlanRepo.deleteAll();
        maintenanceTaskRepo.deleteAll();
        plantReservationRepo.deleteAll();

        PlantInventoryItem item = plantInventoryItemRepo.findOne("1");
        item.setEquipmentCondition(EquipmentCondition.UNSERVICEABLEREPAIRABLE);
        plantInventoryItemRepo.save(item);

        MaintenanceTask mt = new MaintenanceTask();
        PlantReservation pr = PlantReservation.of(
                IdentifierFactory.nextID(),
                BusinessPeriod.of(weekFromNow, almostMonthFromNow),
                item.getId(),
                null);
        plantReservationRepo.save(pr);
        mt.setId(IdentifierFactory.nextID());
        maintenanceTaskRepo.save(mt);

        boolean a = inventoryRepoImpl.isAPlantAvailableRelaxed(
                plantInventoryEntryRepo.findOne("1"),
                BusinessPeriod.of(monthFromNow, twoMonthFromNow));
        assert(!a);
    }

    @Test
    public void queryFindAvaliablePlants(){
        assertThat((inventoryRepo.findAvailablePlants("Mini excavator", LocalDate.parse("2017-03-24"), LocalDate.parse("2017-03-25"))).size()).isEqualTo(2);
    }

    @Test
    public void queryFindForCorrectiveRepairsCostsForLastFiveYearIsNotNull()
    {
        assertThat(inventoryRepo.findCorrectiveRepairsCostsForLastFiveYears()).isNotNull();
    }
    @Test
    public void queryFindForCorrectiveRepairsCostsForLastFiveYears()
    {
        assertThat(inventoryRepo.findCorrectiveRepairsCostsForLastFiveYears().size()).isLessThan(5);
    }

    @Test
    public void queryFindForCorrectiveRepairsCostsFor2017()
    {
        int  costsFor2017 = (inventoryRepo.findCorrectiveRepairsCostsForLastFiveYears().get(0).getCost()).intValue();
        assertThat(costsFor2017).isEqualTo(800);
    }

    @Test
    public void queryFindForCorrectiveRepairsNumberForLastFiveYearIsNotNull()
    {
        assertThat(inventoryRepo.findCorrectiveRepairsNumberForLastFiveYears()).isNotNull();
    }

    @Test
    public void queryFindForCorrectiveRepairsNumberForLastFiveYears()
    {
        assertThat(inventoryRepo.findCorrectiveRepairsNumberForLastFiveYears().size()).isLessThan(5);
    }

    @Test
    public void queryFindForCorrectiveRepairsNumberFor2017()
    {
        assertThat(inventoryRepo.findCorrectiveRepairsNumberForLastFiveYears().get(0).getNumber()).isEqualTo(2);
    }
}

