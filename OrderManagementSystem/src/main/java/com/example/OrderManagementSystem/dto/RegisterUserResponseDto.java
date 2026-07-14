package com.example.OrderManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterUserResponseDto {
    private String name;
    private Long id;
}
