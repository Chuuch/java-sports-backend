package com.sports.platform.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserModel {
    private final UUID id;
    private final String email;
    private String password;
    private final String firstName;
    private final String lastName;
    private final UserRole role;
    private final LocalDateTime createdAt;

    public UserModel(
            UUID id,
            String email,
            String password,
            String firstName,
            String lastName,
            UserRole role,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.createdAt = createdAt;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public enum UserRole {
        USER, ADMIN
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public UserRole getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
