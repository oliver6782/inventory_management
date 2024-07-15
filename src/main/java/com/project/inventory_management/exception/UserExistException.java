package com.project.inventory_management.exception;

public class UserExistException extends RuntimeException {
    public UserExistException(String message) {
        super(message);
    }

}
