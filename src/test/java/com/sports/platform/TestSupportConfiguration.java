package com.sports.platform;

import com.sports.platform.application.port.output.EventPublisher;
import com.sports.platform.domain.event.ScoreUpdatedEvent;
import com.sports.platform.domain.event.UserRegisteredEvent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
@EnableCaching
public class TestSupportConfiguration {

	@Bean
	@Primary
	public EventPublisher testEventPublisher() {
		return new EventPublisher() {
			@Override
			public void publishScoreUpdate(ScoreUpdatedEvent event) {
			}

			@Override
			public void publishUserRegistered(UserRegisteredEvent event) {
			}
		};
	}
}
