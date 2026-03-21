package com.sports.platform.application.service;

import com.sports.platform.application.port.output.EventPublisher;
import com.sports.platform.application.port.output.MatchRepository;
import com.sports.platform.domain.event.ScoreUpdatedEvent;
import com.sports.platform.domain.model.Match;
import com.sports.platform.http.dto.CreateMatchRequest;
import com.sports.platform.http.dto.MatchResponse;
import com.sports.platform.infrastructure.persistence.mapper.MatchHttpMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchHttpMapper matchHttpMapper;
    private final EventPublisher eventPublisher;

    @Transactional
    public MatchResponse createMatch(CreateMatchRequest request) {
        Match match = new Match(
                UUID.randomUUID(),
                request.homeTeamId(),
                request.awayTeamId(),
                request.startTime()
        );

        Match savedMatch = matchRepository.save(match);
        return matchHttpMapper.toResponse(savedMatch);
    }

    @Cacheable(value = "liveMatches", key = "'all_live'")
    @Transactional(readOnly = true)
    public List<MatchResponse> getLiveMatches() {
        return matchRepository.findAllLiveMatches()
                .stream()
                .map(matchHttpMapper::toResponse)
                .toList();
    }

    @CacheEvict(value = "liveMatches", allEntries = true)
    @Transactional
    public MatchResponse updateScore(UUID matchId, int homeScore, int awayScore) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        match.updateScore(homeScore, awayScore);
        Match updatedMatch = matchRepository.save(match);

        eventPublisher.publishScoreUpdate(new ScoreUpdatedEvent(
                updatedMatch.getId(),
                updatedMatch.getScore().homeScore(),
                updatedMatch.getScore().awayScore(),
                LocalDateTime.now()
        ));

        return matchHttpMapper.toResponse(updatedMatch);
    }
}
