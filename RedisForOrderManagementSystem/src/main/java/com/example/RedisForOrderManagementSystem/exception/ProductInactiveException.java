package com.example.RedisForOrderManagementSystem.exception;

public class ProductInactiveException extends RuntimeException {
    public ProductInactiveException(String message) {
        super(message);
    }
}
