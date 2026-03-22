package com.sports.platform.infrastructure.messaging.kafka;

import com.sports.platform.application.port.output.EventPublisher;
import com.sports.platform.domain.event.ScoreUpdatedEvent;
import com.sports.platform.domain.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String SCORE_UPDATES_TOPIC = "sports.matches.scores";
    private static final String USER_REGISTERED_TOPIC = "users.registered";

    @Override
    public void publishScoreUpdate(ScoreUpdatedEvent event) {
        log.info("Publishing score update for match: {}", event.matchId());

        kafkaTemplate.send(SCORE_UPDATES_TOPIC, event.matchId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.debug("Successfully sent score update event to Kafka: {}", result.getRecordMetadata());
                    } else {
                        log.error("Failed to send event to Kafka", ex);
                    }
                });
    }

    @Override
    public void publishUserRegistered(UserRegisteredEvent event) {
        log.info("Publishing user registered: {}", event.email());

        kafkaTemplate.send(USER_REGISTERED_TOPIC, event.email(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.debug("Successfully sent user registered event to Kafka: {}", result.getRecordMetadata());
                    } else {
                        log.error("Failed to send event to Kafka", ex);
                    }
                });
    }
}
