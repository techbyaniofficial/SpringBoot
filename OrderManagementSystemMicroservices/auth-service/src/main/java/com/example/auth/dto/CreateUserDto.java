package com.example.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {
    @NotBlank
    @Size(max = 100)
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
