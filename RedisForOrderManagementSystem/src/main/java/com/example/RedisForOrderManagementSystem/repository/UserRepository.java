package com.example.RedisForOrderManagementSystem.repository;

import com.example.RedisForOrderManagementSystem.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    Page<User> findAll(Pageable pageable);
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
