package com.sports.platform.infrastructure.persistence.adapter;

import com.sports.platform.application.port.output.UserRepository;
import com.sports.platform.domain.model.UserModel;
import com.sports.platform.infrastructure.persistence.entity.UserEntity;
import com.sports.platform.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.sports.platform.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserPersistenceAdapter implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserPersistenceMapper userMapper;

    public UserPersistenceAdapter(JpaUserRepository jpaUserRepository, UserPersistenceMapper userMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserModel save(UserModel userModel) {
        UserEntity entity = userMapper.toEntity(userModel);
        UserEntity saved = jpaUserRepository.save(entity);
        return userMapper.toDomain(saved);
    }
    @Override
    public Optional<UserModel> findById(UUID id) {
        return jpaUserRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}
