package com.example.sales.domain.web.controller;

import com.example.inventory.application.PurchaseOrderDTO;
import com.example.sales.domain.web.controller.dto.CatalogQueryDTO;
import com.example.services.PlantCatalogService;
import com.example.services.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Rybachello on 3/3/2017.
 */
@Controller
@RequestMapping("/dashboard")
public class	DashboardController	{
    //@Autowired
    //PlantCatalogService plantCatalog;
    //@Autowired
    //SalesService salesService;

    @GetMapping("/catalog/form")
    public String getQueryForm(Model model)	{
        model.addAttribute("catalogQuery",	new CatalogQueryDTO());
        return	"dashboard/catalog/query-form";
    }
    @RequestMapping("/catalog/query")
    public String executeQuery(CatalogQueryDTO query,Model model)
    {
        //todo: fix here
        return "dashboard/catalog/query-result";
    }
    @RequestMapping("/orders")
    String	createPO(PurchaseOrderDTO toDTO, Model	model)	{
        //todo: convert to DTO show the result
        model.addAttribute("purchaseOrderDetails", toDTO);
        return	"dashboard/catalog/purchase-order-details";
    }
 // ...
}
