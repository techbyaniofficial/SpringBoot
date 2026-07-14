package com.example.product.service;

import com.example.product.dto.ProductDto;
import com.example.product.dto.ProductRequestDto;
import com.example.product.entity.Product;
import com.example.product.exception.DuplicateProductNameException;
import com.example.product.exception.ProductNotFoundException;
import com.example.product.repository.ProductRepository;
import com.example.product.security.JwtPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private boolean isAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal instanceof JwtPrincipal jwtPrincipal && "ADMIN".equals(jwtPrincipal.role());
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

    public ProductDto getById(Long id) {
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
