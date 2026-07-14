package com.example.OrderManagementSystem.service;

import com.example.OrderManagementSystem.entities.User;
import com.example.OrderManagementSystem.exception.EmailAlreadyExistsException;
import com.example.OrderManagementSystem.exception.UserNotFoundException;
import com.example.OrderManagementSystem.repository.UserRepository;
import com.example.OrderManagementSystem.dto.CreateUserDto;
import com.example.OrderManagementSystem.dto.UserDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
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

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return map(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    private UserDto map(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}








