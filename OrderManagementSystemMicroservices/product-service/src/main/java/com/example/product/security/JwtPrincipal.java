package com.example.product.security;

public record JwtPrincipal(Long userId, String email, String role) {
}
