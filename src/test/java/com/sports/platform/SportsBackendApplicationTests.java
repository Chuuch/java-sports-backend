package com.sports.platform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import(TestSupportConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
class SportsBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
