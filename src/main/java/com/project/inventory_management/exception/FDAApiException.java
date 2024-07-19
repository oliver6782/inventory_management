package com.project.inventory_management.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FDAApiException extends RuntimeException {
    private String code;

    public FDAApiException(String message) {
        super(message);
    }

}
