package com.example.employee.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Payload accepted from clients when creating or updating an Employee.
 * Kept separate from the response representation so that request-only
 * concerns (validation) never leak into what we send back.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payload for creating or updating an employee")
public class EmployeeRequestDto {
	
	@Schema(
	        description = "Employee ID. Leave null for create, provide it for update.",
	        example = "101",
	        nullable = true
	    )
	    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "Employee's first name", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Schema(description = "Employee's last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be a valid email address")
    @Schema(description = "Employee's unique email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @Schema(description = "10 digit phone number", example = "9876543210")
    private String phoneNumber;

    @NotBlank(message = "Department is required")
    @Schema(description = "Department the employee belongs to", example = "Engineering")
    private String department;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive value")
    @Schema(description = "Monthly salary", example = "75000.00")
    private Double salary;

    @NotNull(message = "Date of joining is required")
    @PastOrPresent(message = "Date of joining cannot be in the future")
    @Schema(description = "Date the employee joined", example = "2023-01-15")
    private LocalDate dateOfJoining;
}
