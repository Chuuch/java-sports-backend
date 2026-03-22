package com.sports.platform.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile("!test")
public class KafkaConfig {
    @Bean
    public NewTopic scoreUpdatesTopic() {
        return TopicBuilder.name("sports.matches.scores")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
