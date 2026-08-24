package com.example.inventory.exception;

public class DuplicateProductException extends RuntimeException {
	public DuplicateProductException(Long productId) {
        super("Product already exists with id: " + productId);
    }
}
