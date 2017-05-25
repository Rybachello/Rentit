package com.example.maintenance.application.services;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.application.exceptions.InvalidPurchaseOrderStatusException;
import com.example.common.application.exceptions.PlantNotAvailableException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.dto.PlantInventoryItemDTO;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.maintenance.application.dto.MaintenanceDTO;
import com.example.maintenance.utils.Constants;
import com.example.sales.application.gateways.InvoicingGateway;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * Created by Rybachello on 5/21/2017.
 */
@Service
public class MaintenanceService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    InvoicingGateway invoicingGateway;

    @Value("${gmail.username}")
    String gmailUsername;


    public Boolean createMaintenance(MaintenanceDTO dto) {
        return restTemplate.postForObject(Constants.MAINTENANCE_URL + "/api/maintenances/tasks", dto, Boolean.class);
    }

    public void replacePlantInventoryItem(PlantInventoryItemDTO dto) throws InvalidPurchaseOrderStatusException {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAllPurchaseOrdersByIdAndPeriod(dto.get_id(), dto.getMaintenancePeriod().getStartDate(), dto.getMaintenancePeriod().getEndDate());

        for (PurchaseOrder po : purchaseOrders) {
            try {
                inventoryService.createPlantReservation(po);
            } catch (PlantNotAvailableException e) {
                po.closePurchaseOrder();
                po.resetPlanInventoryItem();
                sendNotAvailablePlantNotification(po.getCustomer().getEmail());
            }
        }

        purchaseOrderRepository.flush();
    }



    public PlantInventoryItem getAvailable(List<PlantInventoryItem> itemList, BusinessPeriod rentalPeriod) throws PlantNotAvailableException {
        for (PlantInventoryItem item : itemList) {
            if (!isMaintenanceExists(item, rentalPeriod)) {
                return item;
            }
        }

        throw new PlantNotAvailableException("Requested plant is unavailable");
    }

    private Boolean isMaintenanceExists(PlantInventoryItem item, BusinessPeriod rentalPeriod) {

        MaintenanceDTO dto = new MaintenanceDTO();
        dto.setPlantId(item.getId());
        dto.setMaintenancePeriod(BusinessPeriodDTO.of(rentalPeriod.getStartDate(), rentalPeriod.getEndDate()));

        return restTemplate.postForObject(Constants.MAINTENANCE_URL + "/api/maintenances/tasks/check", dto, Boolean.class);

    }

    private void sendNotAvailablePlantNotification(String email) {

        JavaMailSender mailSender = new JavaMailSenderImpl();

        MimeMessage rootMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {
            helper = new MimeMessageHelper(rootMessage, true);
            helper.setFrom(gmailUsername + "@gmail.com");
            helper.setTo(email);
            helper.setText("Dear customer, \n\n We are sorry to inform you that your order is closed due to broken plant item and we cannot find any replacement \n\n Kindly yours, \n\n RentIt team!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        invoicingGateway.sendNotification(rootMessage);

    }


}
