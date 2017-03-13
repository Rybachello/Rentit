package com.example.sales.rest.controller;

import com.example.DemoApplication;
import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@WebAppConfiguration
@DirtiesContext
public class SalesRestControllerTests {
    @Autowired
    PlantInventoryEntryRepository repo;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Sql("plants-dataset.sql")
    public void testGetAllPlants() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/sales/plants?name=Exc&startDate=2017-04-14&endDate=2017-04-25"))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", isEmptyOrNullString()))
                .andReturn();

        List<PlantInventoryEntryDTO> plants = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<PlantInventoryEntryDTO>>() {
        });

        assertThat(plants.size()).isEqualTo(2);

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        //todo: fix here
        //order.setPlant(plants.get(1));
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now()));

        mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}