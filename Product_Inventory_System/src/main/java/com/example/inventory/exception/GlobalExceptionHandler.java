package com.example.inventory.exception;

import com.example.inventory.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Product not found -> 404
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(
            ProductNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, List.of(ex.getMessage()), request);
    }

    // 2. Duplicate product -> 409
    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateProduct(
            DuplicateProductException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, List.of(ex.getMessage()), request);
    }

    // 3. Insufficient stock -> 409
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(
            InsufficientStockException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, List.of(ex.getMessage()), request);
    }

    // 4. Empty inventory -> 404
    @ExceptionHandler(EmptyInventoryException.class)
    public ResponseEntity<ErrorResponse> handleEmptyInventory(
            EmptyInventoryException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, List.of(ex.getMessage()), request);
    }

    // 5. Invalid product data (business rule) -> 400
    @ExceptionHandler(InvalidProductDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidProductData(
            InvalidProductDataException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()), request);
    }

    // Bean validation failures (@NotBlank, @Min, @Positive) -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> messages = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        return buildResponse(HttpStatus.BAD_REQUEST, messages, request);
    }

    // Fallback for anything unhandled -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                List.of("An unexpected error occurred: " + ex.getMessage()), request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status, List<String> messages, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                status.value(), status.getReasonPhrase(), messages, request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }
}
