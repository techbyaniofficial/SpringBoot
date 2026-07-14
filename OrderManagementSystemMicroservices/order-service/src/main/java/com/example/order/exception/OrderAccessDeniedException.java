package com.example.order.exception;

public class OrderAccessDeniedException extends RuntimeException {
    public OrderAccessDeniedException(String message) {
        super(message);
    }
}
