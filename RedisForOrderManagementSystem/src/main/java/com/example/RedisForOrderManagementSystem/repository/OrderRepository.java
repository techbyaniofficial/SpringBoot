package com.example.RedisForOrderManagementSystem.repository;

import com.example.RedisForOrderManagementSystem.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

}
