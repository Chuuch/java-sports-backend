package com.sports.platform.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchScoreEmbeddable {
    @Column(name = "home_score")
    private int homeScore;
    @Column(name = "away_score")
    private int awayScore;
}
