package com.example.RedisForOrderManagementSystem.exception;

public class OrderAccessDeniedException extends RuntimeException {
    public OrderAccessDeniedException(String message) {
        super(message);
    }
}
