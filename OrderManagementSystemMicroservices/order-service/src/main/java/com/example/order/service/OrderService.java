package com.example.order.service;

import com.example.order.client.ProductClient;
import com.example.order.kafka.OrderEventPublisher;
import com.example.order.dto.CreateOrderDto;
import com.example.order.dto.OrderDto;
import com.example.order.dto.ProductDto;
import com.example.order.dto.UserDto;
import com.example.order.entity.Order;
import com.example.order.exception.OrderAccessDeniedException;
import com.example.order.exception.OrderNotFoundException;
import com.example.order.repository.OrderRepository;
import com.example.order.security.JwtPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final HttpServletRequest httpServletRequest;
    private final OrderEventPublisher orderEventPublisher;

    private JwtPrincipal getLoggedInPrincipal() {
        Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        if (principal instanceof JwtPrincipal jwtPrincipal) {
            return jwtPrincipal;
        }
        throw new OrderAccessDeniedException("Authentication required");
    }

    private String getBearerToken() {
        String header = httpServletRequest.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new OrderAccessDeniedException("Missing authorization token");
    }

    @Transactional
    public OrderDto createOrder(CreateOrderDto dto) {
        JwtPrincipal principal = getLoggedInPrincipal();
        ProductDto product = productClient.getProduct(dto.getProductId(), getBearerToken());

        Order order = new Order();
        order.setUserId(principal.userId());
        order.setUserName(principal.name());
        order.setUserEmail(principal.email());
        order.setProductId(product.getId());
        order.setProductName(product.getName());
        order.setPriceAtPurchase(product.getPrice());

        Order saved = orderRepository.save(order);
        orderEventPublisher.publishOrderCreated(saved);
        return map(saved);
    }

    public List<OrderDto> getMyOrders() {
        JwtPrincipal principal = getLoggedInPrincipal();
        return orderRepository.findByUserId(principal.userId()).stream()
                .map(this::map)
                .toList();
    }

    public OrderDto getMyOrder(Long id) {
        JwtPrincipal principal = getLoggedInPrincipal();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + id));

        if (!Objects.equals(order.getUserId(), principal.userId())) {
            throw new OrderAccessDeniedException("You do not have access to this order");
        }

        return map(order);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::map)
                .toList();
    }

    public List<OrderDto> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::map)
                .toList();
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + id));
        orderRepository.delete(order);
    }

    private OrderDto map(Order order) {
        return new OrderDto(
                order.getId(),
                order.getProductId(),
                order.getProductName(),
                order.getPriceAtPurchase(),
                new UserDto(order.getUserId(), order.getUserName(), order.getUserEmail())
        );
    }
}
