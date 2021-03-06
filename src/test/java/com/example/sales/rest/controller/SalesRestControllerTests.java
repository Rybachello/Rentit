package com.example.sales.rest.controller;

import com.example.RentIt;
import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.POStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
@SpringBootTest(classes = RentIt.class)
@WebAppConfiguration
@Sql(scripts = "plants-dataset.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SalesRestControllerTests {
    @Autowired
    PlantInventoryEntryRepository repo;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    //@Autowired
    //ObjectMapper mapper;
    @Autowired @Qualifier("_halObjectMapper")
    ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testCreatePurchaseOrderReturnsHttpCreated() throws Exception {
        String jsonString = "{\"plant\":{\"_id\":\"1\"}, \"rentalPeriod\": {\"startDate\": \"2116-09-22\", \"endDate\": \"2116-09-24\"}}";

        mockMvc.perform(post("/api/sales/orders").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreatePurchaseOrderWithTakenPlantReturnsHttpConflict() throws Exception {
        String jsonString = "{\"plant\":{\"_id\":\"2\"}, \"rentalPeriod\": {\"startDate\": \"2016-09-22\", \"endDate\": \"2016-09-24\"}}";

        mockMvc.perform(post("/api/sales/orders").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetAllPurchaseOrders() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/sales/orders"))
                .andExpect(status().isOk())
                .andReturn();

        List<PurchaseOrderDTO> orders = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<PurchaseOrderDTO>>() {
        });

        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    public void testGetPurchaseOrdersById() throws Exception {
        String id = "123";
        MvcResult result = mockMvc.perform(get("/api/sales/orders/" + id))
                .andExpect(status().isOk())
                .andReturn();

        PurchaseOrderDTO orders = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {
        });
        assertThat(orders.get_id()).isEqualTo(id);
    }

    @Test
    public void testGetPurchaseOrdersByIdNotFound() throws Exception {
        String id = "777";
        MvcResult result = mockMvc.perform(get("/api/sales/orders/" + id))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testPurchaseOrderAcceptance() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/api/inventory/plants?name=exc&startDate=2016-09-22&endDate=2016-09-24"))
                .andReturn();
        List<PlantInventoryEntryDTO> plants =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<PlantInventoryEntryDTO>>() { });

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setPlant(plants.get(0));
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now()));

        result = mockMvc.perform(post("/api/sales/orders")
                .content(mapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(result.getResponse().getHeader("Location")).isNotEmpty();

        order = mapper.readValue(result.getResponse().getContentAsString(), PurchaseOrderDTO.class);
        assertThat(order.get_xlink("accept")).isNotNull();
        assertThat(order.getStatus() == POStatus.PENDING);

        result = mockMvc.perform(post(order.get_xlink("accept").getHref()))
                .andExpect(status().isOk())
                .andReturn();

        order = mapper.readValue(result.getResponse().getContentAsString(), PurchaseOrderDTO.class);
        assertThat(order.getStatus() == POStatus.OPEN);
    }

    @Test
    public void testPurchaseOrderRejection() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/api/inventory/plants?name=exc&startDate=2016-09-22&endDate=2016-09-24"))
                .andReturn();
        List<PlantInventoryEntryDTO> plants =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<PlantInventoryEntryDTO>>() { });

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setPlant(plants.get(0));
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now()));

        result = mockMvc.perform(post("/api/sales/orders")
                .content(mapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(result.getResponse().getHeader("Location")).isNotEmpty();

        order = mapper.readValue(result.getResponse().getContentAsString(), PurchaseOrderDTO.class);
        assertThat(order.get_xlink("reject")).isNotNull();
        assertThat(order.getStatus() == POStatus.PENDING);

        result = mockMvc.perform(delete(order.get_xlink("reject").getHref()))
                .andExpect(status().isOk())
                .andReturn();

        order = mapper.readValue(result.getResponse().getContentAsString(), PurchaseOrderDTO.class);
        assertThat(order.getStatus() == POStatus.REJECTED);
    }

    @Test
    public void testPurchaseOrderClosing() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/api/inventory/plants?name=exc&startDate=2016-09-22&endDate=2016-09-24"))
                .andReturn();
        List<PlantInventoryEntryDTO> plants =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<PlantInventoryEntryDTO>>() { });

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setPlant(plants.get(0));
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now()));

        result = mockMvc.perform(post("/api/sales/orders")
                .content(mapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(result.getResponse().getHeader("Location")).isNotEmpty();

        order = mapper.readValue(result.getResponse().getContentAsString(), PurchaseOrderDTO.class);
        assertThat(order.get_xlink("accept")).isNotNull();
        assertThat(order.getStatus() == POStatus.PENDING);

        result = mockMvc.perform(post(order.get_xlink("accept").getHref()))
                .andExpect(status().isOk())
                .andReturn();

        order = mapper.readValue(result.getResponse().getContentAsString(), PurchaseOrderDTO.class);
        assertThat(order.getStatus() == POStatus.OPEN);

        assertThat(order.get_xlink("close")).isNotNull();

        result = mockMvc.perform(delete(order.get_xlink("close").getHref()))
                .andExpect(status().isOk())
                .andReturn();
        order = mapper.readValue(result.getResponse().getContentAsString(), PurchaseOrderDTO.class);

        assertThat(order.getStatus() == POStatus.CLOSED);
    }
}