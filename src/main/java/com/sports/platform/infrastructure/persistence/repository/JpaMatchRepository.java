package com.sports.platform.infrastructure.persistence.repository;

import com.sports.platform.domain.model.Match;
import com.sports.platform.infrastructure.persistence.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaMatchRepository extends JpaRepository<MatchEntity, UUID> {
    List<MatchEntity> findAllByStatus(Match.MatchStatus status);
}
