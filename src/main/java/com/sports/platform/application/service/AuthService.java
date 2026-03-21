package com.sports.platform.application.service;

import com.sports.platform.application.port.output.EventPublisher;
import com.sports.platform.application.port.output.UserRepository;
import com.sports.platform.domain.event.UserRegisteredEvent;
import com.sports.platform.http.dto.AuthResponse;
import com.sports.platform.http.dto.LoginRequest;
import com.sports.platform.http.dto.RegisterRequest;
import com.sports.platform.domain.model.UserModel;
import com.sports.platform.infrastructure.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already in use");
        }

        UserModel user = new UserModel(
                UUID.randomUUID(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                UserModel.UserRole.USER,
                LocalDateTime.now()
        );

        userRepository.save(user);

        eventPublisher.publishUserRegistered(new UserRegisteredEvent(user.getEmail(), user.getFirstName()));

        UserDetails userDetails = User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = "dummy-refresh-token";

        return new AuthResponse(accessToken, refreshToken, user.getEmail());
    }

    public AuthResponse authenticate(LoginRequest request) {
     authManager.authenticate(
             new UsernamePasswordAuthenticationToken(request.email(), request.password())
     );

     var user = userRepository.findByEmail(request.email())
             .orElseThrow(() -> new RuntimeException("User not found"));

     UserDetails userDetails = User
             .withUsername(user.getEmail())
             .password(user.getPassword())
             .authorities(new ArrayList<>())
             .build();

     String accessToken = jwtService.generateToken(userDetails);
     String refreshToken = "dummy-refresh-token";

     return new AuthResponse(accessToken, refreshToken, user.getEmail());
    }
}
