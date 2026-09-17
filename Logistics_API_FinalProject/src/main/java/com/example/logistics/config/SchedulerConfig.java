package com.example.logistics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class that enables Spring's annotation-driven 
 * scheduled task execution capabilities.
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
    // No explicit methods required. 
    // @EnableScheduling tells Spring to scan for @Scheduled annotations across your beans.
}