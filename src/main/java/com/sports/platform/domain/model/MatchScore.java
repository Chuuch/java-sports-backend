package com.sports.platform.domain.model;

import com.sports.platform.domain.exception.InvalidScoreException;

public record MatchScore(int homeScore, int awayScore) {
    public MatchScore {
        if (homeScore < 0 || awayScore < 0) {
            throw new InvalidScoreException("Score cannot be negative");
        }
    }

    public static MatchScore zero() {
        return new MatchScore(0, 0);
    }
}
