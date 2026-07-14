package com.example.order.exception;

public class ProductInactiveException extends RuntimeException {
    public ProductInactiveException(String message) {
        super(message);
    }
}
