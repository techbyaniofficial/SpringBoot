package com.example.OrderManagementSystem.repository;

import com.example.OrderManagementSystem.AbstractIntegrationTest;
import com.example.OrderManagementSystem.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
public class ProductRepositoryIntegrationTest extends
        AbstractIntegrationTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should save product")
    void shouldSaveProduct() {
        Product product = new Product();
        product.setName("iPhone");
        product.setActive(true);
        product.setPrice(BigDecimal.valueOf(70000.0));

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        assertEquals("iPhone", savedProduct.getName());

    }

    @Test
    @DisplayName("Should return active products")
    void shouldReturnActiveProducts() {

        Product p1 = new Product();
        p1.setName("Mouse");
        p1.setPrice(new BigDecimal("2000"));
        p1.setActive(true);

        Product p2 = new Product();
        p2.setName("Keyboard");
        p2.setPrice(new BigDecimal("1000"));
        p2.setActive(true);

        productRepository.saveAll(
                List.of(p1, p2)
        );

        List<Product> products =
                productRepository
                        .findAllByActiveTrueOrderByNameAsc();

        assertEquals(2, products.size());

        assertEquals(
                "Keyboard",
                products.get(0).getName()
        );
    }
}
