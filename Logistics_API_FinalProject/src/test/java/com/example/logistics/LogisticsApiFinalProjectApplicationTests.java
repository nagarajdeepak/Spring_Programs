package com.example.logistics;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test: verifies the full Spring application context (all @Configuration
 * classes, @Component/@Service/@Repository beans, JPA entity mappings, and
 * Spring Security filter chain) wires up without errors.
 */

@SpringBootTest
@ActiveProfiles("dev")
class LogisticsApiFinalProjectApplicationTests {

	@Test
	void contextLoads() {
		//If the ApplicationContext fails to start, this test fails.
	}
}
