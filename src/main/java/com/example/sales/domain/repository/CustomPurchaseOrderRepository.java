package com.example.sales.domain.repository;

import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.PurchaseOrder;

import java.util.List;

/**
 * Created by Rybachello on 3/14/2017.
 */
public interface CustomPurchaseOrderRepository {

    List<PurchaseOrderDTO> findAllPO();
    PurchaseOrderDTO findPurchaseOrderById(String id);

}
