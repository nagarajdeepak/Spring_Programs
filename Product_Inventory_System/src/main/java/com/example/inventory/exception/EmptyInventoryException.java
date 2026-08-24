package com.example.inventory.exception;

public class EmptyInventoryException extends RuntimeException {
	public EmptyInventoryException(String message) {
        super(message);
    }
}
