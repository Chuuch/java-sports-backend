package com.sports.platform.application.port.output;

import com.sports.platform.domain.event.ScoreUpdatedEvent;
import com.sports.platform.domain.event.UserRegisteredEvent;

public interface EventPublisher {
    void publishScoreUpdate(ScoreUpdatedEvent event);
    void publishUserRegistered(UserRegisteredEvent event);
}
