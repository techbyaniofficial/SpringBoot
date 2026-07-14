package com.example.auth.service;

import com.example.auth.dto.CreateUserDto;
import com.example.auth.dto.UserDto;
import com.example.auth.entity.User;
import com.example.auth.exception.EmailAlreadyExistsException;
import com.example.auth.exception.UserNotFoundException;
import com.example.auth.repository.UserRepository;
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
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserDto getCurrentUser() {
        return map(getLoggedInUser());
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
        return userRepository.findAll().stream().map(this::map).toList();
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
