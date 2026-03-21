package com.sports.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SportsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportsBackendApplication.class, args);
	}

}
