package com.sports.platform.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScoreUpdatedEvent(
        UUID matchId,
        int homeScore,
        int awayScore,
        LocalDateTime occurredAt
) {}
