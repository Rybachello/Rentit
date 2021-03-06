package com.example.inventory.domain.repository;

import com.example.RentIt;
import com.example.common.domain.model.BusinessPeriod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RentIt.class)
@Sql(scripts="plants-dataset.sql")
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InventoryRepositoryTests {
    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepo;
    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepo;
    @Autowired
    InventoryRepository inventoryRepo;
    @Autowired
    InventoryRepositoryImpl inventoryRepoImpl;

    @Test
    public void queryUnhiredPlants() {
        assertThat(inventoryRepo.findUnhired().size()).isEqualTo(3);
    }

    @Test
    public void checkIfPlantAvailableStrict() {
        boolean a = inventoryRepoImpl.isAPlantAvailableStrict(
                plantInventoryEntryRepo.findOne("1"),
                BusinessPeriod.of(LocalDate.of(2012, 12, 12), LocalDate.of(2012, 12, 13)));
        assert (a);
    }

    private boolean IsAPlantAvailableStrict() {
        return inventoryRepoImpl.isAPlantAvailableStrict(
                plantInventoryEntryRepo.findOne("1"),
                BusinessPeriod.of(LocalDate.of(2012, 12, 12), LocalDate.of(2012, 12, 22)));
    }


    @Test
    public void queryFindAvaliablePlants() {
        assertThat((inventoryRepo.findAvailablePlants("Mini excavator", LocalDate.parse("2017-03-24"), LocalDate.parse("2017-03-25"))).size()).isEqualTo(2);
    }
}