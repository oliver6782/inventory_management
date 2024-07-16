package com.project.inventory_management.exception;

// This exception aims to handle authenticated requests that find no user by id
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
