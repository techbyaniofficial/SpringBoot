package com.example.OrderManagementSystem.controller;

import com.example.OrderManagementSystem.AbstractIntegrationTest;
import com.example.OrderManagementSystem.dto.ProductRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerIntegrationTest
        extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create product API")
    void shouldCreateProductApi() throws Exception {

        ProductRequestDto dto =
                new ProductRequestDto();

        dto.setName("Monitor");
        dto.setPrice(new BigDecimal("15000"));

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writeValueAsString(dto)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name")
                        .value("Monitor"));
    }
}






