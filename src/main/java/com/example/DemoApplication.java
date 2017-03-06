package com.example;

import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
		PlantInventoryEntryRepository repository = ctx.getBean(PlantInventoryEntryRepository.class);

		PlantInventoryEntry plant = new PlantInventoryEntry();
		plant.setName("Bike");
		plant.setDescription("Nice and shiny");
		plant.setPrice(BigDecimal.valueOf(100));

		repository.save(plant);
	}
}
