package com.platform.auth.service;

import com.platform.auth.dto.AuthResponse;
import com.platform.auth.dto.LoginRequest;
import com.platform.auth.dto.SignupRequest;
import com.platform.auth.model.User;
import com.platform.auth.repository.UserRepository;
import com.platform.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthResponse registerUser(SignupRequest request) {
        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        // Create new user
        Set<User.Role> roles = new HashSet<>();
        roles.add(User.Role.ROLE_USER);

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .roles(roles)
                .enabled(true)
                .accountNonLocked(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        user = userRepository.save(user);
        log.info("New user registered: {}", user.getUsername());

        // Generate token
        String token = tokenProvider.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                .message("User registered successfully")
                .build();
    }

    public AuthResponse loginUser(LoginRequest request) {
        // Find user
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // Check if account is locked
        if (!user.isAccountNonLocked()) {
            throw new RuntimeException("Account is locked");
        }

        // Check if account is enabled
        if (!user.isEnabled()) {
            throw new RuntimeException("Account is disabled");
        }

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Update last login
        user.setLastLoginAt(Instant.now());
        userRepository.save(user);

        log.info("User logged in: {}", user.getUsername());

        // Generate token
        String token = tokenProvider.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                .message("Login successful")
                .build();
    }

    public User validateToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        String username = tokenProvider.getUsernameFromToken(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void createAdminIfNotExists() {
        if (!userRepository.existsByUsername("admin")) {
            Set<User.Role> roles = new HashSet<>();
            roles.add(User.Role.ROLE_USER);
            roles.add(User.Role.ROLE_ADMIN);

            User admin = User.builder()
                    .username("admin")
                    .email("admin@codeplatform.com")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Platform Administrator")
                    .roles(roles)
                    .enabled(true)
                    .accountNonLocked(true)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            userRepository.save(admin);
            log.info("Default admin user created: username=admin, password=admin123");
        }
    }
}
