package com.project.inventory_management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FDAErrorResponseDTO {
    // deleted Error class
    private String code;
    private String message;

}
