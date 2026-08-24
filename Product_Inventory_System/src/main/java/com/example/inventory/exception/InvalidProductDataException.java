package com.example.inventory.exception;

public class InvalidProductDataException extends RuntimeException {
	public InvalidProductDataException(String message) {
        super(message);
    }
}
