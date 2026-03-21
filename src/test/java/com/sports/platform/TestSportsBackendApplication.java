package com.sports.platform;

import org.springframework.boot.SpringApplication;

public class TestSportsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(SportsBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
