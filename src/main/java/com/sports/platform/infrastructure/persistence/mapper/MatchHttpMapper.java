package com.sports.platform.infrastructure.persistence.mapper;

import com.sports.platform.domain.model.Match;
import com.sports.platform.http.dto.MatchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MatchHttpMapper {

    @Mapping(source = "score.homeScore", target = "homeScore")
    @Mapping(source = "score.awayScore", target = "awayScore")
    MatchResponse toResponse(Match domain);
}
