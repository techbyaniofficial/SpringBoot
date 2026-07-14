package com.example.order.client;

import com.example.order.dto.ProductDto;
import com.example.order.exception.ProductInactiveException;
import com.example.order.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ProductClient {

    private final RestClient productRestClient;

    public ProductDto getProduct(Long productId, String bearerToken) {
        try {
            ProductDto product = productRestClient.get()
                    .uri("/api/v1/products/{id}", productId)
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()
                    .body(ProductDto.class);

            if (product == null) {
                throw new ProductNotFoundException("No product with id " + productId);
            }
            if (!product.isActive()) {
                throw new ProductInactiveException("Product is not available for ordering");
            }
            return product;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProductNotFoundException("No product with id " + productId);
        } catch (HttpClientErrorException.Forbidden | HttpClientErrorException.Unauthorized ex) {
            throw new ProductNotFoundException("Unable to access product catalog");
        }
    }
}
