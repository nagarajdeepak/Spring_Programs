package com.example.logistics.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Simple in-memory cache manager backing the Spring cache abstraction
 * (@Cacheable / @CacheEvict, see CustomerServiceImpl). Swap this bean for a
 * Redis/Caffeine CacheManager in production without touching the service layer.
 */

@Configuration
public class CacheConfig {
	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("customers");
	}
}