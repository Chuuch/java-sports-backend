package com.sports.platform.http.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateMatchRequest (
    @NotNull
    UUID homeTeamId,
    @NotNull
    UUID awayTeamId,
    @NotNull @Future
    LocalDateTime startTime
) {}
