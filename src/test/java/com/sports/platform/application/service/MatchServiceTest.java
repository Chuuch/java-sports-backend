package com.sports.platform.application.service;

import com.sports.platform.application.port.output.EventPublisher;
import com.sports.platform.application.port.output.MatchRepository;
import com.sports.platform.domain.event.ScoreUpdatedEvent;
import com.sports.platform.domain.model.Match;
import com.sports.platform.http.dto.CreateMatchRequest;
import com.sports.platform.http.dto.MatchResponse;
import com.sports.platform.infrastructure.persistence.mapper.MatchHttpMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchHttpMapper matchHttpMapper;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private MatchService matchService;

    @Test
    @DisplayName("Should create match successfully")
    void shouldCreateMatch() {
        UUID homeId = UUID.randomUUID();
        UUID awayId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        CreateMatchRequest request = new CreateMatchRequest(homeId, awayId, now);
        
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(matchHttpMapper.toResponse(any(Match.class))).thenReturn(new MatchResponse(
                UUID.randomUUID(), homeId, awayId, now, Match.MatchStatus.LIVE, 0, 0
        ));

        MatchResponse result = matchService.createMatch(request);

        assertNotNull(result);
        verify(matchRepository).save(any(Match.class));
    }

    @Test
    @DisplayName("Should get live matches")
    void shouldGetLiveMatches() {
        Match match = new Match(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now());
        when(matchRepository.findAllLiveMatches()).thenReturn(List.of(match));
        when(matchHttpMapper.toResponse(match)).thenReturn(mock(MatchResponse.class));

        List<MatchResponse> results = matchService.getLiveMatches();

        assertEquals(1, results.size());
        verify(matchRepository).findAllLiveMatches();
    }

    @Test
    @DisplayName("Should update score and publish event")
    void shouldUpdateScore() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId, UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now());
        
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(matchHttpMapper.toResponse(any(Match.class))).thenReturn(mock(MatchResponse.class));

        matchService.updateScore(matchId, 3, 2);

        assertEquals(3, match.getScore().homeScore());
        assertEquals(2, match.getScore().awayScore());
        
        verify(eventPublisher).publishScoreUpdate(any(ScoreUpdatedEvent.class));
        
        ArgumentCaptor<ScoreUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(ScoreUpdatedEvent.class);
        verify(eventPublisher).publishScoreUpdate(eventCaptor.capture());
        
        ScoreUpdatedEvent event = eventCaptor.getValue();
        assertEquals(matchId, event.matchId());
        assertEquals(3, event.homeScore());
        assertEquals(2, event.awayScore());
    }

    @Test
    @DisplayName("Should throw exception when match not found for score update")
    void shouldThrowWhenMatchNotFound() {
        UUID matchId = UUID.randomUUID();
        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> matchService.updateScore(matchId, 1, 1));
    }
}
