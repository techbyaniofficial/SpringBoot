package com.example.OrderManagementSystem.service;

import com.example.OrderManagementSystem.entities.Role;
import com.example.OrderManagementSystem.entities.User;
import com.example.OrderManagementSystem.repository.UserRepository;
import com.example.OrderManagementSystem.security.JwtService;
import com.example.OrderManagementSystem.dto.CreateUserDto;
import com.example.OrderManagementSystem.dto.LoginDto;
import com.example.OrderManagementSystem.dto.LoginResponseDto;
import com.example.OrderManagementSystem.dto.RegisterUserResponseDto;
import com.example.OrderManagementSystem.exception.EmailAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterUserResponseDto registerUser(CreateUserDto createUserDto) {
        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setName(createUserDto.getName());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        return new RegisterUserResponseDto(savedUser.getName(), savedUser.getId());
    }

    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        String jwtToken = jwtService.generateJwtToken((UserDetails) Objects.requireNonNull(authentication.getPrincipal()));
        return new LoginResponseDto(jwtToken);
    }


}
