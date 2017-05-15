package com.example.sales.application.dto;

import com.example.common.rest.ResourceSupport;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by stepan on 16/05/2017.
 */
@Data
public class RemittanceAdviceDTO extends ResourceSupport {
    String invoiceId;
}
