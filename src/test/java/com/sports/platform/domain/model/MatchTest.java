package com.sports.platform.domain.model;

import com.sports.platform.domain.exception.DomainException;
import com.sports.platform.domain.exception.InvalidScoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    @Test
    @DisplayName("Should create match with initial LIVE status and zero score")
    void shouldCreateMatch() {
        UUID homeId = UUID.randomUUID();
        UUID awayId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Match match = new Match(UUID.randomUUID(), homeId, awayId, now);

        assertEquals(Match.MatchStatus.LIVE, match.getStatus());
        assertEquals(0, match.getScore().homeScore());
        assertEquals(0, match.getScore().awayScore());
        assertEquals(homeId, match.getHomeTeamId());
        assertEquals(awayId, match.getAwayTeamId());
    }

    @Test
    @DisplayName("Should throw exception when teams are the same")
    void shouldThrowWhenTeamsSame() {
        UUID teamId = UUID.randomUUID();
        assertThrows(DomainException.class, () -> 
            new Match(UUID.randomUUID(), teamId, teamId, LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should update score when match is LIVE")
    void shouldUpdateScore() {
        Match match = new Match(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now());
        
        match.updateScore(2, 1);
        
        assertEquals(2, match.getScore().homeScore());
        assertEquals(1, match.getScore().awayScore());
    }

    @Test
    @DisplayName("Should throw exception when updating score and match is not LIVE")
    void shouldThrowWhenUpdatingScoreAndNotLive() {
        Match match = new Match(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now());
        match.finishMatch();
        
        assertThrows(DomainException.class, () -> match.updateScore(1, 0));
    }

    @Test
    @DisplayName("Should throw InvalidScoreException when scores are negative")
    void shouldThrowWhenScoreNegative() {
        Match match = new Match(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now());
        
        assertThrows(InvalidScoreException.class, () -> match.updateScore(-1, 0));
        assertThrows(InvalidScoreException.class, () -> match.updateScore(0, -5));
    }
}
