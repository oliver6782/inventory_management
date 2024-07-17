package com.project.inventory_management.exception;

public class MedicationExistException extends RuntimeException {
    public MedicationExistException(String message) {
        super(message);
    }

}
