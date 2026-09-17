package com.example.logistics.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.logistics.entity.AppUser;
import com.example.logistics.entity.enums.Role;
import com.example.logistics.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;

/**
 * CommandLineRunner beans run once after the Spring context is fully
 * initialized. @Profile("dev") scopes this to the dev profile only, so
 * demo accounts never get seeded in prod.
 */

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
	
	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) {
		if (appUserRepository.count() == 0) {
			appUserRepository.save(AppUser.builder()
					.username("admin")
					.password(passwordEncoder.encode("admin123"))
					.role(Role.ROLE_ADMIN)
					.build());
			
			appUserRepository.save(AppUser.builder()
					.username("operator")
					.password(passwordEncoder.encode("operator123"))
					.role(Role.ROLE_OPERATOR)
					.build());
		}
	}

}