package com.example.logistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Bootstraps the Logistics Management REST API.
 *
 * Demonstrates several Spring Boot / Spring Framework concepts at the
 * application level:
 *  - @SpringBootApplication: auto-configuration + component scanning + config class, all in one
 *  - @EnableJpaRepositories: turns on Spring Data JPA repository infrastructure
 *  - @EnableJpaAuditing: powers @CreatedDate / @LastModifiedDate on entities
 *  - @EnableCaching: activates the Spring cache abstraction (see CacheConfig)
 *  - @EnableAsync: activates asynchronous method execution (see notification service)
 *  - @ConfigurationPropertiesScan: picks up @ConfigurationProperties classes (see FileStorageProperties)
 */

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@ConfigurationPropertiesScan(basePackages = "com.example.logistics.config")
public class LogisticsApiFinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogisticsApiFinalProjectApplication.class, args);
	}

}