package com.example.RedisForOrderManagementSystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderDto {
    @NotNull(message = "productId is required")
    private Long productId;
}
