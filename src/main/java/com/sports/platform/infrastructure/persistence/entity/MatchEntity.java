package com.sports.platform.infrastructure.persistence.entity;

import com.sports.platform.domain.model.Match;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchEntity {
    @Id
    private UUID id;
    @Column(name = "home_team_id", nullable = false)
    private UUID homeTeamId;
    @Column(name = "away_team_id", nullable = false)
    private UUID awayTeamId;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Match.MatchStatus status;
    @Embedded
    private MatchScoreEmbeddable score;
}
