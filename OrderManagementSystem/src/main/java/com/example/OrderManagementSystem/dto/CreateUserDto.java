package com.example.OrderManagementSystem.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {
    @NotBlank
    @NotNull
    @Size(max = 100)
    private String name;
    @Email
    @NotBlank
    @NotNull
    private String email;
    @NotBlank
    @NotNull
    private String password;
}
