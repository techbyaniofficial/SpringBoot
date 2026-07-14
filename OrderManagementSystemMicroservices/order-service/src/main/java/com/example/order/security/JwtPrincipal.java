package com.example.order.security;

public record JwtPrincipal(Long userId, String email, String role, String name) {
}
