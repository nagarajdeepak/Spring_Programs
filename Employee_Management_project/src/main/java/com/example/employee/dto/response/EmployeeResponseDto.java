package com.example.employee.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Representation of an Employee sent back to clients.
 * Includes read-only auditing metadata that clients never submit.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Employee data returned by the API")
public class EmployeeResponseDto {

    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Schema(description = "Employee's first name", example = "John")
    private String firstName;

    @Schema(description = "Employee's last name", example = "Doe")
    private String lastName;

    @Schema(description = "Employee's unique email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "10 digit phone number", example = "9876543210")
    private String phoneNumber;

    @Schema(description = "Department the employee belongs to", example = "Engineering")
    private String department;

    @Schema(description = "Monthly salary", example = "75000.00")
    private Double salary;

    @Schema(description = "Date the employee joined", example = "2023-01-15")
    private LocalDate dateOfJoining;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Timestamp the record was created")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Timestamp the record was last updated")
    private LocalDateTime updatedAt;
}
