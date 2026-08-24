package com.example.movieticket.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.movieticket.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	//Handles @Vaild failures (@NotBlank, @Positive, etc.)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidErrors(MethodArgumentNotValidException ex, HttpServletRequest request){
		List<String> message = ex.getBindingResult().getFieldErrors().stream().map(FieldError -> FieldError.getDefaultMessage()).toList();
		
		ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Validation Failed", message, request.getRequestURI());
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(TicketNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleTicketNotFound(TicketNotFoundException ex, HttpServletRequest request){
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
	}
	
	@ExceptionHandler(SeatAlreadyBookedException.class)
	public ResponseEntity<ErrorResponse> handleSeatAlreadyBooked(SeatAlreadyBookedException ex, HttpServletRequest request){
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
	}
	
	@ExceptionHandler(InvalidTicketPriceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPrice(
            InvalidTicketPriceException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MovieNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleMovieNotAvailable(
            MovieNotAvailableException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicateBookingException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateBooking(
            DuplicateBookingException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong: " + ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                List.of(message),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, status);
    }
}