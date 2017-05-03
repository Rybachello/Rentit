package com.example.sales.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Rybachello on 5/4/2017.
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(force = true)
public class RemittanceDTO {

    String invoiceId;
    String note;
    String id;
}
