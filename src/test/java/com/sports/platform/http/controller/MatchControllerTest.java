package com.sports.platform.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sports.platform.application.service.MatchService;
import com.sports.platform.domain.model.Match;
import com.sports.platform.http.dto.CreateMatchRequest;
import com.sports.platform.http.dto.MatchResponse;
import com.sports.platform.infrastructure.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import com.resend.Resend;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class MatchControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private MatchService matchService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private Resend resend;

    @MockitoBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockitoBean
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Should return live matches")
    @WithMockUser
    void shouldReturnLiveMatches() throws Exception {
        MatchResponse response = new MatchResponse(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now(), Match.MatchStatus.LIVE, 0, 0
        );

        when(matchService.getLiveMatches()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/matches/live"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("LIVE"));
    }

    @Test
    @DisplayName("Should create match when user is ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldCreateMatchWhenAdmin() throws Exception {
        CreateMatchRequest request = new CreateMatchRequest(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now());
        MatchResponse response = new MatchResponse(
                UUID.randomUUID(), request.homeTeamId(), request.awayTeamId(),
                request.startTime(), Match.MatchStatus.LIVE, 0, 0
        );

        when(matchService.createMatch(any(CreateMatchRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/matches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("LIVE"));
    }

    @Test
    @DisplayName("Should return 403 when creating match as non-ADMIN")
    @WithMockUser(roles = "USER")
    void shouldReturn403WhenNotAdmin() throws Exception {
        CreateMatchRequest request = new CreateMatchRequest(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now());

        mockMvc.perform(post("/api/v1/matches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should update score when user is ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateScoreWhenAdmin() throws Exception {
        UUID matchId = UUID.randomUUID();
        MatchResponse response = new MatchResponse(
                matchId, UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now(), Match.MatchStatus.LIVE, 2, 1
        );

        when(matchService.updateScore(eq(matchId), eq(2), eq(1))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/matches/{id}/score", matchId)
                        .with(csrf())
                        .param("homeScore", "2")
                        .param("awayScore", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.homeScore").value(2))
                .andExpect(jsonPath("$.awayScore").value(1));
    }
}
