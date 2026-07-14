package com.example.RedisForOrderManagementSystem.service;

import com.example.RedisForOrderManagementSystem.dto.CreateOrderDto;
import com.example.RedisForOrderManagementSystem.dto.OrderDto;
import com.example.RedisForOrderManagementSystem.dto.UserDto;
import com.example.RedisForOrderManagementSystem.entities.Order;
import com.example.RedisForOrderManagementSystem.entities.Product;
import com.example.RedisForOrderManagementSystem.entities.User;
import com.example.RedisForOrderManagementSystem.exception.OrderAccessDeniedException;
import com.example.RedisForOrderManagementSystem.exception.OrderNotFoundException;
import com.example.RedisForOrderManagementSystem.exception.ProductInactiveException;
import com.example.RedisForOrderManagementSystem.exception.ProductNotFoundException;
import com.example.RedisForOrderManagementSystem.exception.UserNotFoundException;
import com.example.RedisForOrderManagementSystem.repository.OrderRepository;
import com.example.RedisForOrderManagementSystem.repository.ProductRepository;
import com.example.RedisForOrderManagementSystem.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private User getLoggedInUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No user found for the authenticated principal"));
    }

    @Transactional
    public OrderDto createOrder(CreateOrderDto dto) {
        User user = getLoggedInUser();

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("No product with id " + dto.getProductId()));
        if (!product.isActive()) {
            throw new ProductInactiveException("Product is not available for ordering");
        }

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setPriceAtPurchase(product.getPrice());

        Order saved = orderRepository.save(order);

        return map(saved);
    }

    public List<OrderDto> getMyOrders() {
        User user = getLoggedInUser();

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    public OrderDto getMyOrder(Long id) {
        User user = getLoggedInUser();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + id));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderAccessDeniedException("You do not have access to this order");
        }

        return map(order);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public List<OrderDto> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::map)
                .toList();
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + id));
        orderRepository.delete(order);
    }

    private OrderDto map(Order order) {
        User u = order.getUser();
        Product p = order.getProduct();
        return new OrderDto(
                order.getId(),
                p.getId(),
                p.getName(),
                order.getPriceAtPurchase(),
                new UserDto(u.getId(), u.getName(), u.getEmail())
        );
    }
}
