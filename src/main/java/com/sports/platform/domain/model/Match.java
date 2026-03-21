package com.sports.platform.domain.model;

import com.sports.platform.domain.exception.DomainException;

import java.time.LocalDateTime;
import java.util.UUID;

public class Match {
    private final UUID id;
    private final UUID homeTeamId;
    private final UUID awayTeamId;
    private final LocalDateTime startTime;
    private MatchStatus status;
    private MatchScore score;

    public enum MatchStatus {
        SCHEDULED, LIVE, FINISHED, CANCELLED
    }

    public Match(
            UUID id,
            UUID homeTeamId,
            UUID awayTeamId,
            LocalDateTime startTime
    ) {
        if (homeTeamId.equals(awayTeamId)) {
            throw new DomainException("A team cannot play against itself");
        }
        this.id = id;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.startTime = startTime;
        this.status = MatchStatus.LIVE;
        this.score = MatchScore.zero();
    }

    public void startMatch() {
        if (this.status != MatchStatus.SCHEDULED) {
            throw new DomainException("Match can only start if scheduled");
        }
        this.status = MatchStatus.LIVE;
    }

    public void updateScore(int home, int away) {
        if (this.status != MatchStatus.LIVE) {
            throw new DomainException("Score can only be updated for live matches");
        }
        this.score = new MatchScore(home, away);
    }

    public void finishMatch() {
        this.status = MatchStatus.FINISHED;
    }

    public UUID getId() { return id; }
    public UUID getHomeTeamId() { return homeTeamId; }
    public UUID getAwayTeamId() { return awayTeamId; }
    public MatchStatus getStatus() { return status; }
    public MatchScore getScore() { return score; }
    public LocalDateTime getStartTime() { return startTime; }
}
