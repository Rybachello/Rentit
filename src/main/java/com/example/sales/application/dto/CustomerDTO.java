package com.example.sales.application.dto;

import com.example.common.rest.ResourceSupport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class CustomerDTO extends ResourceSupport {
    private String _id;
    private String token;
    private String email;
}
