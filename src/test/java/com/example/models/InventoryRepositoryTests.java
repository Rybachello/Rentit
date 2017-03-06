package com.example.models;

import com.example.DemoApplication;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.inventory.domain.repository.PlantInventoryItemRepository;
import com.example.inventory.domain.repository.PlantReservationRepository;
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
@SpringBootTest(classes = DemoApplication.class)
@Sql(scripts="plants-dataset.sql")
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InventoryRepositoryTests {
    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepo;
    @Autowired
    PlantInventoryItemRepository plantInventorItemRepo;
    @Autowired
    PlantReservationRepository plantReservationRepo;
    @Autowired
    InventoryRepository inventoryRepo;

    @Test
    public void queryUnhiredPlants() {
        assertThat(inventoryRepo.findUnhired().size()).isEqualTo(2);
    }


    @Test
    public void queryFindAvaliablePlants(){
        assertThat((inventoryRepo.findAvailablePlants("Mini excavator", LocalDate.parse("2017-03-24"), LocalDate.parse("2017-03-25"))).size()).isEqualTo(2);
    }


}
