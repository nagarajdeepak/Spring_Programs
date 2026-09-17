package com.example.employee.exception;

/**
 * User-defined exception thrown when a requested Employee resource
 * cannot be located.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
