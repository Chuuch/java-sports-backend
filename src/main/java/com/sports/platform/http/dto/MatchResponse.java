package com.sports.platform.http.dto;

import com.sports.platform.domain.model.Match;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatchResponse(
        UUID id,
        UUID homeTeamId,
        UUID awayTeamId,
        LocalDateTime startTime,
        Match.MatchStatus status,
        int homeScore,
        int awayScore
) {}
