package com.example.sales.application.services;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.application.exceptions.CustomerNotFoundException;
import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.common.application.exceptions.PlantNotAvailableException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.rest.ExtendedLink;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.rest.controller.SalesRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpMethod.*;

/**
 * Created by minhi_000 on 09.03.2017.
 */
@Service
public class PurchaseOrderAssembler extends ResourceAssemblerSupport<PurchaseOrder, PurchaseOrderDTO> {
    @Autowired
    private
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PurchaseOrderAssembler() {
        super(SalesRestController.class, PurchaseOrderDTO.class);
    }

    @Override
    public PurchaseOrderDTO toResource(PurchaseOrder purchaseOrder) {

        PurchaseOrderDTO newDTO = new PurchaseOrderDTO();
        newDTO.set_id(purchaseOrder.getId());

        LocalDate startDate = purchaseOrder.getRentalPeriod().getStartDate();
        LocalDate endDate = purchaseOrder.getRentalPeriod().getEndDate();
        BusinessPeriodDTO businessPeriod = BusinessPeriodDTO.of(startDate, endDate);

        PlantInventoryEntry plant = purchaseOrder.getPlant().getPlantInfo();

        newDTO.setStatus(purchaseOrder.getStatus());
        newDTO.setTotal(purchaseOrder.getTotal());

        newDTO.setRentalPeriod(businessPeriod);
        newDTO.setPlant(plantInventoryEntryAssembler.toResource(plant));

        newDTO.setConstructionSite(purchaseOrder.getConstructionSite());

        try {
            newDTO.add(new ExtendedLink(
                    linkTo(methodOn(SalesRestController.class)
                            .fetchPurchaseOrder(newDTO.get_id(), null)).toString(),
                    "self", GET));

            switch (newDTO.getStatus()) {
                case PENDING:
                    newDTO.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .acceptPurchaseOrder(newDTO.get_id())).toString(),
                            "accept", POST));
                    newDTO.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .rejectPurchaseOrder(newDTO.get_id())).toString(),
                            "reject", DELETE));
                    newDTO.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .updatePurchaseOrder(newDTO,newDTO.get_id())).toString(),
                            "update", PUT));
                    break;
                case OPEN:
                    newDTO.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .cancelPurchaseOrder(newDTO.get_id())).toString(),
                            "close", DELETE));
                    newDTO.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .dispatchPurchaseOrder(newDTO.get_id())).toString(),
                            "dispatch", POST));
                    newDTO.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .updatePurchaseOrder(newDTO,newDTO.get_id())).toString(),
                            "update", PUT));
                    break;
                case DISPATCHED:
                    newDTO.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .deliverPurchaseOrder(newDTO.get_id())).toString(),
                            "deliver", POST));
                    newDTO.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .rejectPOByCustomer(newDTO.get_id(), null)).toString(),
                            "rejectByCustomer", POST));
                    break;
                case DELIVERED:
                    newDTO.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .returnPurchaseOrder(newDTO.get_id())).toString(),
                            "return", POST));
                    break;
                default:
                    break;

            }
        } catch (Exception e) {
        } catch (PurchaseOrderNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidPurchaseOrderStatusException e) {
            e.printStackTrace();
        } catch (CustomerNotFoundException e) {
            e.printStackTrace();
        }
        catch (PlantNotAvailableException e) {
            e.printStackTrace();
        }
        return newDTO;
    }
}
