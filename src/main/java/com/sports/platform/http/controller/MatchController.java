package com.sports.platform.http.controller;

import com.sports.platform.application.service.MatchService;
import com.sports.platform.http.dto.CreateMatchRequest;
import com.sports.platform.http.dto.MatchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatchResponse> createMatch(@Valid @RequestBody CreateMatchRequest request) {
        return new ResponseEntity<>(matchService.createMatch(request), HttpStatus.CREATED);
    }

    @GetMapping("/live")
    public ResponseEntity<List<MatchResponse>> getLiveMatches() {
        return ResponseEntity.ok(matchService.getLiveMatches());
    }

    @PatchMapping("/{id}/score")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatchResponse> updateScore(
            @PathVariable UUID id,
            @RequestParam int homeScore,
            @RequestParam int awayScore
            ) {
        return ResponseEntity.ok(matchService.updateScore(id, homeScore, awayScore));
    }
}
