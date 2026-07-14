package com.example.OrderManagementSystem.controller;

import com.example.OrderManagementSystem.AbstractIntegrationTest;
import com.example.OrderManagementSystem.dto.CreateUserDto;
import com.example.OrderManagementSystem.dto.LoginDto;
import com.example.OrderManagementSystem.dto.LoginResponseDto;
import com.example.OrderManagementSystem.dto.RegisterUserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment =
                SpringBootTest.WebEnvironment.RANDOM_PORT
)
class AuthControllerIntegrationTest
        extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should register user")
    void shouldRegisterUser() {

        RestClient restClient =
                RestClient.builder()
                        .baseUrl(baseUrl())
                        .build();

        CreateUserDto dto =
                new CreateUserDto();

        dto.setName("Aniket");
        dto.setEmail("register@test.com");
        dto.setPassword("password123");

        RegisterUserResponseDto response =
                restClient.post()
                        .uri("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .retrieve()
                        .body(RegisterUserResponseDto.class);

        assertNotNull(response);

        assertEquals(
                "Aniket",
                response.getName()
        );
    }

    @Test
    @DisplayName("Should reject duplicate email")
    void shouldRejectDuplicateEmail() {

        RestClient restClient =
                RestClient.builder()
                        .baseUrl(baseUrl())
                        .build();

        CreateUserDto dto =
                new CreateUserDto();

        dto.setName("Aniket");
        dto.setEmail("duplicate@test.com");
        dto.setPassword("password123");

        // FIRST REGISTER

        restClient.post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .body(RegisterUserResponseDto.class);

        // SECOND REGISTER SHOULD FAIL

        assertThrows(
                HttpClientErrorException.Conflict.class,
                () -> restClient.post()
                        .uri("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .retrieve()
                        .body(String.class)
        );

        int i = 9;
    }

    @Test
    @DisplayName("Should login successfully")
    void shouldLoginSuccessfully() {

        RestClient restClient =
                RestClient.builder()
                        .baseUrl(baseUrl())
                        .build();

        CreateUserDto registerDto =
                new CreateUserDto();

        registerDto.setName("Aniket");
        registerDto.setEmail("aniket@test.com");
        registerDto.setPassword("password123");

        restClient.post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerDto)
                .retrieve()
                .body(RegisterUserResponseDto.class);

        LoginDto loginDto =
                new LoginDto();

        loginDto.setEmail("aniket@test.com");
        loginDto.setPassword("password123");

        LoginResponseDto response =
                restClient.post()
                        .uri("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(loginDto)
                        .retrieve()
                        .body(LoginResponseDto.class);

        assertNotNull(response);

        assertNotNull(response.getJwt());
    }

    @Test
    @DisplayName("Should access protected endpoint with JWT")
    void shouldAccessProtectedEndpoint() {

        RestClient restClient =
                RestClient.builder()
                        .baseUrl(baseUrl())
                        .build();

        // REGISTER USER

        CreateUserDto registerDto =
                new CreateUserDto();

        registerDto.setName("User");
        registerDto.setEmail("user@test.com");
        registerDto.setPassword("password123");

        restClient.post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerDto)
                .retrieve()
                .body(RegisterUserResponseDto.class);

        // LOGIN

        LoginDto loginDto =
                new LoginDto();

        loginDto.setEmail("user@test.com");
        loginDto.setPassword("password123");

        LoginResponseDto loginResponse =
                restClient.post()
                        .uri("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(loginDto)
                        .retrieve()
                        .body(LoginResponseDto.class);

        assertNotNull(loginResponse);

        String token =
                loginResponse.getJwt();

        assertNotNull(token);

        // ACCESS PROTECTED ENDPOINT

        String response =
                restClient.get()
                        .uri("/api/v1/products")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token
                        )
                        .retrieve()
                        .body(String.class);

        assertNotNull(response);
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }
}