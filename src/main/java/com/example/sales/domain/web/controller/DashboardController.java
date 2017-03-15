package com.example.sales.domain.web.controller;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.sales.application.services.SalesService;
import com.example.sales.domain.web.dto.CatalogQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Rybachello on 3/3/2017.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController	{
    @Autowired
    InventoryService inventoryService;
    @Autowired
    SalesService salesService;
    @Autowired
    PlantInventoryEntryAssembler plantInvAssembler;

    @GetMapping("/catalog/form")
    public String getQueryForm(Model model)	{
        model.addAttribute("catalogQuery",	new CatalogQueryDTO());
        return	"dashboard/catalog/query-form";
    }
    @RequestMapping("/catalog/query")
    public String executeQuery(CatalogQueryDTO query,Model model)
    {
       List<PlantInventoryEntryDTO> availablePlants =  inventoryService.createListOfAvailablePlants(query);
       model.addAttribute("plants", availablePlants);
       model.addAttribute("q", query);
       model.addAttribute("po", new PurchaseOrderDTO());
       return "dashboard/catalog/query-result";
    }
    @RequestMapping("/orders")
    public String createPO(PurchaseOrderDTO toDTO, Model model) throws PlantNotFoundException {
        PurchaseOrderDTO po = salesService.getPurchaseOrder(toDTO);
        model.addAttribute("purchaseOrderDetails", po);
        return	"dashboard/catalog/purchase-order-details";
    }
}
