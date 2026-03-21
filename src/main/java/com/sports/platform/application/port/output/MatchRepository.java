package com.sports.platform.application.port.output;

import com.sports.platform.domain.model.Match;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepository {
    Match save(Match match);
    Optional<Match> findById(UUID id);
    List<Match> findAllLiveMatches();
}
