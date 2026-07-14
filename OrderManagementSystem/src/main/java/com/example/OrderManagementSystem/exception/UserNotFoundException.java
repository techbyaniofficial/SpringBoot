package com.example.OrderManagementSystem.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String errorMsg) {
        super(errorMsg);
    }
}
