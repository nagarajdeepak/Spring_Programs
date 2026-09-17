package com.example.logistics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/** Drives the springdoc-generated Swagger UI at /swagger-ui.html. */

@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI logisticsOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("Logistics API")
				.description("REST API supporting a comperhensive logistics management system:"+"customers, warehouse, inventory, shipments, tracking, and delivery.")
				.version("v1.0.0")
				.contact(new Contact().name("Logistics Platform Team")))
				.addSecurityItem(new SecurityRequirement().addList("basicAuth"))
				.components(new Components()
						.addSecuritySchemes("basicAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")));
	}
}