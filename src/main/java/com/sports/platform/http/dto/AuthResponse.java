package com.sports.platform.http.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String email
) {}
