package com.sports.platform.infrastructure.persistence.mapper;

import com.sports.platform.domain.model.Match;
import com.sports.platform.domain.model.MatchScore;
import com.sports.platform.infrastructure.persistence.entity.MatchEntity;
import com.sports.platform.infrastructure.persistence.entity.MatchScoreEmbeddable;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MatchPersistenceMapper {
    MatchEntity toEntity(Match domain);
    Match toDomain(MatchEntity entity);

    MatchScoreEmbeddable mapScore(MatchScore score);
    MatchScore mapScore(MatchScoreEmbeddable entityScore);
}
