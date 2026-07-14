package com.example.OrderManagementSystem.controller;

import com.example.OrderManagementSystem.dto.ProductDto;
import com.example.OrderManagementSystem.dto.ProductRequestDto;
import com.example.OrderManagementSystem.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    /** Public catalog: active products only (USER or ADMIN). */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getActiveCatalog() {
        return ResponseEntity.ok(productService.getActiveCatalog());
    }

    /** Admin: all products including inactive. */
    @GetMapping("/admin")
    public ResponseEntity<List<ProductDto>> getAllForAdmin() {
        return ResponseEntity.ok(productService.getAllForAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    /** Soft delete: marks product inactive (ADMIN). */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        productService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
