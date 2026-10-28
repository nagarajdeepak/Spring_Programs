package com.example.employeeservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class EmployeeServiceT2Application {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceT2Application.class, args);
	}

	// WebClient used to call the Department-Service; base URL comes from application.yml
	@Bean
	public WebClient webClient(@Value("${department-service.url}") String departmentServiceUrl) {
		return WebClient.builder().baseUrl(departmentServiceUrl).build();
	}
}
