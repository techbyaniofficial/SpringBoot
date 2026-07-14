package com.example.OrderManagementSystem.exception;

public class ProductInactiveException extends RuntimeException {
    public ProductInactiveException(String message) {
        super(message);
    }
}
