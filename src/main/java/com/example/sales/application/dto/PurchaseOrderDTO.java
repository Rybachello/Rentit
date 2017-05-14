package com.example.sales.application.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.rest.ResourceSupport;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.sales.domain.model.POStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by Rybachello on 3/7/2017.
 */
@Data
public class PurchaseOrderDTO extends ResourceSupport {
    public PurchaseOrderDTO(){}

    public PurchaseOrderDTO(String id, POStatus status, LocalDate startDate, LocalDate endDate, BigDecimal price, String constructionSite){
        this._id = id;
        this.status = status==null?POStatus.PENDING:status;
        this.rentalPeriod = BusinessPeriodDTO.of(startDate, endDate);
        this.constructionSite = constructionSite;
        this.total = BigDecimal.valueOf(ChronoUnit.DAYS.between(startDate, endDate) + 1).multiply(price);
    }

    private String _id;
    private POStatus status;
    private BusinessPeriodDTO rentalPeriod;
    private BigDecimal total;
    private String constructionSite;
    private PlantInventoryEntryDTO plant;
}
