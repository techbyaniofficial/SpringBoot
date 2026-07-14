package com.example.OrderManagementSystem.service;

import com.example.OrderManagementSystem.AbstractIntegrationTest;
import com.example.OrderManagementSystem.dto.ProductDto;
import com.example.OrderManagementSystem.dto.ProductRequestDto;
import com.example.OrderManagementSystem.entities.Product;
import com.example.OrderManagementSystem.exception.DuplicateProductNameException;
import com.example.OrderManagementSystem.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ProductServiceIntegrationTest
        extends AbstractIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should create product")
    void shouldCreateProduct() {

        ProductRequestDto dto =
                new ProductRequestDto();

        dto.setName("Laptop");
        dto.setPrice(new BigDecimal("70000"));

        ProductDto result =
                productService.create(dto);

        assertNotNull(result.getId());

        Product dbProduct =
                productRepository.findById(result.getId())
                        .orElseThrow();

        assertEquals(
                "Laptop",
                dbProduct.getName()
        );
    }

    @Test
    @DisplayName("Should throw duplicate exception")
    void shouldThrowDuplicateException() {

        Product product = new Product();

        product.setName("iPhone");
        product.setPrice(new BigDecimal("100000"));

        productRepository.save(product);

        ProductRequestDto dto =
                new ProductRequestDto();

        dto.setName("iphone");
        dto.setPrice(new BigDecimal("90000"));

        assertThrows(
                DuplicateProductNameException.class,
                () -> productService.create(dto)
        );
    }

}