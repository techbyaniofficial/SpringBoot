package com.example.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterUserResponseDto {
    private String name;
    private Long id;
}
