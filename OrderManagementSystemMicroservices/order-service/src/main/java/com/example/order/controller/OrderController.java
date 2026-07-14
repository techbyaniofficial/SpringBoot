package com.example.order.controller;

import com.example.order.dto.CreateOrderDto;
import com.example.order.dto.OrderDto;
import com.example.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderDto>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<OrderDto> getMyOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getMyOrder(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrdersByUser(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
