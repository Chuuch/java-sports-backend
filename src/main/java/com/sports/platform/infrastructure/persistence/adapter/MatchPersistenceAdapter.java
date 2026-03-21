package com.sports.platform.infrastructure.persistence.adapter;

import com.sports.platform.application.port.output.MatchRepository;
import com.sports.platform.domain.model.Match;
import com.sports.platform.infrastructure.persistence.entity.MatchEntity;
import com.sports.platform.infrastructure.persistence.mapper.MatchPersistenceMapper;
import com.sports.platform.infrastructure.persistence.repository.JpaMatchRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MatchPersistenceAdapter implements MatchRepository {
    private final JpaMatchRepository jpaMatchRepository;
    private final MatchPersistenceMapper matchMapper;

    public MatchPersistenceAdapter(
            JpaMatchRepository jpaMatchRepository,
            MatchPersistenceMapper matchMapper
    ) {
        this.jpaMatchRepository = jpaMatchRepository;
        this.matchMapper = matchMapper;
    }

    @Override
    public Match save(Match match) {
        MatchEntity entity = matchMapper.toEntity(match);
        MatchEntity saved = jpaMatchRepository.save(entity);
        return matchMapper.toDomain(saved);
    }

    @Override
    public Optional<Match> findById(UUID id) {
        return jpaMatchRepository.findById(id).map(matchMapper::toDomain);
    }

    @Override
    public List<Match> findAllLiveMatches() {
        return jpaMatchRepository.findAllByStatus(Match.MatchStatus.LIVE)
                .stream()
                .map(matchMapper::toDomain)
                .collect(Collectors.toList());
    }
}
