package com.example.employee.constant;

/**
 * Centralized constants used across the application
 * (messages, defaults, etc.) so they aren't hardcoded/scattered.
 */
public final class AppConstants {

    private AppConstants() {
        // prevent instantiation
    }

    // Success messages
    public static final String EMPLOYEE_CREATED_SUCCESS = "Employee created successfully";
    public static final String EMPLOYEE_UPDATED_SUCCESS = "Employee updated successfully";
    public static final String EMPLOYEE_DELETED_SUCCESS = "Employee deleted successfully";

    // Error messages
    public static final String EMPLOYEE_NOT_FOUND = "Employee not found with id: ";
    public static final String EMPLOYEE_NOT_FOUND_EMAIL = "Employee not found with email: ";
    public static final String EMPLOYEE_EMAIL_DUPLICATE = "An employee already exists with email: ";
    public static final String VALIDATION_FAILED = "Validation failed";
    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred. Please try again later.";

    // Defaults
    public static final String DEFAULT_SORT_FIELD = "id";
}
