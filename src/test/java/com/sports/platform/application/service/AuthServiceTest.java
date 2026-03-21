package com.sports.platform.application.service;

import com.sports.platform.application.port.output.EventPublisher;
import com.sports.platform.application.port.output.UserRepository;
import com.sports.platform.domain.event.UserRegisteredEvent;
import com.sports.platform.domain.model.UserModel;
import com.sports.platform.http.dto.AuthResponse;
import com.sports.platform.http.dto.LoginRequest;
import com.sports.platform.http.dto.RegisterRequest;
import com.sports.platform.infrastructure.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUser() {
        RegisterRequest request = new RegisterRequest("test@test.com", "password", "John", "Doe");
        
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("test@test.com", response.email());
        assertEquals("token", response.accessToken());
        
        verify(userRepository).save(any(UserModel.class));
        verify(eventPublisher).publishUserRegistered(any(UserRegisteredEvent.class));
    }

    @Test
    @DisplayName("Should throw exception when registering with existing email")
    void shouldThrowWhenEmailExists() {
        RegisterRequest request = new RegisterRequest("test@test.com", "password", "John", "Doe");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should authenticate user successfully")
    void shouldAuthenticateUser() {
        LoginRequest request = new LoginRequest("test@test.com", "password");
        UserModel user = new UserModel(UUID.randomUUID(), "test@test.com", "encodedPassword", "John", "Doe", UserModel.UserRole.USER, LocalDateTime.now());
        
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("token");

        AuthResponse response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("token", response.accessToken());
        verify(authManager).authenticate(any());
    }

    @Test
    @DisplayName("Should throw exception when authenticating non-existent user")
    void shouldThrowWhenUserNotFound() {
        LoginRequest request = new LoginRequest("test@test.com", "password");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.authenticate(request));
    }
}
