package com.example.product.exception;

public class DuplicateProductNameException extends RuntimeException {
    public DuplicateProductNameException(String message) {
        super(message);
    }
}
