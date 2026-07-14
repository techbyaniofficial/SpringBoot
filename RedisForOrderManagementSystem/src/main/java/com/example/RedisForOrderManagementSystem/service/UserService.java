package com.example.RedisForOrderManagementSystem.service;

import com.example.RedisForOrderManagementSystem.entities.User;
import com.example.RedisForOrderManagementSystem.exception.EmailAlreadyExistsException;
import com.example.RedisForOrderManagementSystem.exception.UserNotFoundException;
import com.example.RedisForOrderManagementSystem.repository.UserRepository;
import com.example.RedisForOrderManagementSystem.dto.CreateUserDto;
import com.example.RedisForOrderManagementSystem.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private User getLoggedInUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserDto getCurrentUser() {
        User user = getLoggedInUser();
        return map(user);
    }

    @CacheEvict(value = "users", key = "#result.id")
    @Transactional
    public UserDto updateCurrentUser(CreateUserDto dto) {
        User user = getLoggedInUser();

        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        return map(user);
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Cacheable(value = "users", key = "#id")
    public UserDto getUserById(Long id) {
        log.info("Getting user from DB for id {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return map(user);
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    private UserDto map(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}








