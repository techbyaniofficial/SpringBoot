package com.example.RedisForOrderManagementSystem.service;

import com.example.RedisForOrderManagementSystem.dto.ProductDto;
import com.example.RedisForOrderManagementSystem.dto.ProductRequestDto;
import com.example.RedisForOrderManagementSystem.entities.Product;
import com.example.RedisForOrderManagementSystem.exception.DuplicateProductNameException;
import com.example.RedisForOrderManagementSystem.exception.ProductNotFoundException;
import com.example.RedisForOrderManagementSystem.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }
    public List<ProductDto> getActiveCatalog() {
        return productRepository.findAllByActiveTrueOrderByNameAsc().stream()
                .map(this::map)
                .toList();
    }

    public List<ProductDto> getAllForAdmin() {
        return productRepository.findAll().stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .map(this::map)
                .toList();
    }
    @Cacheable(value = "products", key = "#id") // product::1
    public ProductDto getById(Long id) {
        log.info("Getting product from DB for id {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No product with id " + id));
        if (!product.isActive() && !isAdmin()) {
            throw new ProductNotFoundException("No product with id " + id);
        }
        return map(product);
    }

    @Transactional
    public ProductDto create(ProductRequestDto dto) {
        String name = Objects.requireNonNull(dto.getName()).trim();
        if (productRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateProductNameException("A product with this name already exists");
        }
        Product product = new Product();
        product.setName(name);
        product.setPrice(dto.getPrice());
        product.setActive(dto.getActive() != null ? dto.getActive() : true);
        return map(productRepository.save(product));
    }

    @CacheEvict(value = "products", key = "#id")
    @Transactional
    public ProductDto update(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No product with id " + id));
        String name = Objects.requireNonNull(dto.getName()).trim();
        if (productRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateProductNameException("A product with this name already exists");
        }
        product.setName(name);
        product.setPrice(dto.getPrice());
        if (dto.getActive() != null) {
            product.setActive(dto.getActive());
        }
        return map(productRepository.save(product));
    }

    @CacheEvict(value = "products", key = "#id")
    @Transactional
    public void deactivate(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No product with id " + id));
        product.setActive(false);
    }

    private ProductDto map(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.isActive()
        );
    }
}
