package com.bookingmx.reservations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the BookingMx API.
 * <p>
 * This class intercepts exceptions thrown by controllers and converts
 * them into appropriate HTTP responses with structured error messages.
 * It handles validation errors, business exceptions, and unexpected errors.
 * </p>
 * 
 * <h3>Handled Exception Types:</h3>
 * <ul>
 *   <li><b>BadRequestException</b> - Returns 400 with error message</li>
 *   <li><b>NotFoundException</b> - Returns 404 with error message</li>
 *   <li><b>MethodArgumentNotValidException</b> - Returns 400 with field errors</li>
 *   <li><b>Generic Exception</b> - Returns 500 with error details</li>
 * </ul>
 * 
 * @author BookingMx Team
 * @version 1.0.0
 * @since 1.0.0
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorBody(ex.getMessage(), 400));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorBody(ex.getMessage(), 404));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationError(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        field -> field.getDefaultMessage(),
                        (a, b) -> a // merge strategy
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", 400,
                        "message", "Validation failed",
                        "errors", errors
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage() != null ? e.getMessage() : "Unexpected error");
        error.put("type", e.getClass().getSimpleName());
        error.put("status", 500);
        error.put("timestamp", java.time.Instant.now());

        // Print full stack trace for debugging
        System.err.println("=== UNHANDLED EXCEPTION ===");
        e.printStackTrace();
        System.err.println("=========================");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private Map<String, Object> errorBody(String message, int status) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status,
                "message", message
        );
    }
}
