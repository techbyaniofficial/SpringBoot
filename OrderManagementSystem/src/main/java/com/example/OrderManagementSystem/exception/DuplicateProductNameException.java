package com.example.OrderManagementSystem.exception;

public class DuplicateProductNameException extends RuntimeException {
    public DuplicateProductNameException(String message) {
        super(message);
    }
}
