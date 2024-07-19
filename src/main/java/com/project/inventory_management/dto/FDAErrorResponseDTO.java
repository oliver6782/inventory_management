package com.project.inventory_management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FDAErrorResponseDTO {
    private FDAError error;
    // deleted Error class
    private String code;
    private String message;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FDAError {
        private String code;
        private String message;
    }
}
