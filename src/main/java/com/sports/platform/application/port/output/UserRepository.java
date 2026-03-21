package com.sports.platform.application.port.output;

import com.sports.platform.domain.model.UserModel;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    UserModel save(UserModel userModel);
    Optional<UserModel> findById(UUID id);
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);
}
