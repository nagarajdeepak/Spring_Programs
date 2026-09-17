package com.example.employee.exception;

/**
 * User-defined exception thrown when attempting to create an Employee
 * that violates a uniqueness constraint (e.g. duplicate email).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}