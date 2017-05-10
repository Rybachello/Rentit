package com.example.sales;

import com.example.RentIt;
import com.example.inventory.domain.model.EquipmentCondition;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.inventory.domain.repository.PlantInventoryItemRepository;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

@ContextConfiguration(classes = RentIt.class)
@WebAppConfiguration
public class CreationOfPurchaseOrderSteps {

    @Autowired
    private WebApplicationContext wac;

    private WebClient customerBrowser;
    HtmlPage customerPage;

    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;
    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepository;

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Before  // Use `Before` from Cucumber library
    public void setUp() {
        customerBrowser = MockMvcWebClientBuilder.webAppContextSetup(wac).build();
    }

    @After  // Use `After` from Cucumber library
    public void tearOff() {
        purchaseOrderRepository.deleteAll();
        plantInventoryItemRepository.deleteAll();
        plantInventoryEntryRepository.deleteAll();
    }

    @Given("^the following plant catalog$")
    public void the_following_plant_catalog(List<PlantInventoryEntry> entries) throws Throwable {
        plantInventoryEntryRepository.save(entries);
    }

    @Given("^the following inventory$")
    public void the_following_inventory(DataTable table) throws Throwable {
        for (Map<String, String> row: table.asMaps(String.class, String.class))
            plantInventoryItemRepository.save(
                    PlantInventoryItem.of(
                            row.get("id"),
                            row.get("serialNumber"),
                            EquipmentCondition.valueOf(row.get("equipmentCondition")),
                            plantInventoryEntryRepository.findOne(row.get("plantInfo"))
                    )
            );
    }

    @Given("^a customer is in the \"([^\"]*)\" web page$")
    public void a_customer_is_in_the_web_page(String arg1) throws Throwable {
        customerPage = customerBrowser.getPage("http://localhost/dashboard/catalog/form");

    }

    @Given("^no purchase order exists in the system$")
    public void no_purchase_order_exists_in_the_system() throws Throwable {
        purchaseOrderRepository.deleteAll();
    }

    @When("^the customer queries the plant catalog for an \"([^\"]*)\" available from \"([^\"]*)\" to \"([^\"]*)\"$")
    public void the_customer_queries_the_plant_catalog_for_an_available_from_to(String plantName, String startDate, String endDate) throws Throwable {
        HtmlTextInput nameInput = (HtmlTextInput)customerPage.getElementById("name");
        HtmlDateInput startInput = (HtmlDateInput)customerPage.getElementById("rental-start-date");
        HtmlDateInput endInput = (HtmlDateInput) customerPage.getElementById("rental-end-date");
        HtmlButton submit = (HtmlButton)customerPage.getElementById("submit-button");

        nameInput.setValueAttribute(plantName);
        startInput.setValueAttribute(startDate);
        endInput.setValueAttribute(endDate);

        customerPage = submit.click();
    }

    @Then("^(\\d+) plants are shown$")
    public void plants_are_shown(int plantNum) throws Throwable {
        List<?> rows = customerPage.getByXPath("//tr[contains(@class, 'table-row')]");

        Assert.assertEquals(plantNum, rows.size());
    }

    @When("^the customer selects a \"([^\"]*)\"$")
    public void the_customer_selects_a(String plantDescription) throws Throwable {
        List<?> descriptionCellsList = customerPage.getByXPath("//td[text()[contains(.,'" + plantDescription + "')]]");
        HtmlTableDataCell firstDescriptionCell = (HtmlTableDataCell)descriptionCellsList.get(0);
        HtmlTableRow plantRow = (HtmlTableRow)firstDescriptionCell.getParentNode();
        HtmlButton submit = (HtmlButton)plantRow.querySelector("button");

        customerPage = submit.click();
    }

    @Then("^a purchase order should be created with a total price of (\\d+)\\.(\\d+)$")
    public void a_purchase_order_should_be_created_with_a_total_price_of(int priceFull, int priceDecimals) throws Throwable {
        HtmlTableDataCell priceElement = (HtmlTableDataCell)customerPage.getElementById("price-element");
        NumberFormat numberFormat = NumberFormat.getInstance();

        Assert.assertEquals(numberFormat.parse(priceFull + "." + priceDecimals), numberFormat.parse(priceElement.getTextContent()));
    }
}
