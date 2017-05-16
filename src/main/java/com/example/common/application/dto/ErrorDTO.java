package com.example.common.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Rybachello on 5/16/2017.
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
public class ErrorDTO {

    String error;
}
