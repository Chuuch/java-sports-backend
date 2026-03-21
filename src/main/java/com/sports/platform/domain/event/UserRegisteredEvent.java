package com.sports.platform.domain.event;

public record UserRegisteredEvent(
        String email,
        String firstName
) {
}
