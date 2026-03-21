package com.sports.platform.infrastructure.persistence.mapper;

import com.sports.platform.domain.model.UserModel;
import com.sports.platform.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserPersistenceMapper {
    UserEntity toEntity(UserModel domain);
    UserModel toDomain(UserEntity entity);
}
