package com.example.OrderManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal priceAtPurchase;
    private UserDto user;
}
