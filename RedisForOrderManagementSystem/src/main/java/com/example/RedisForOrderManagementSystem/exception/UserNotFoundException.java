package com.example.RedisForOrderManagementSystem.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String errorMsg) {
        super(errorMsg);
    }
}
