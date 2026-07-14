package com.example.RedisForOrderManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponseDto {
    private String code;
    private String message;
}
