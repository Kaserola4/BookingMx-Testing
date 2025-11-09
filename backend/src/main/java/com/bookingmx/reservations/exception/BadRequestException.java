package com.bookingmx.reservations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a client request contains invalid data.
 * <p>
 * This exception maps to HTTP 400 Bad Request status and is used
 * for business rule violations such as invalid dates or attempting
 * to update canceled reservations.
 * </p>
 * 
 * @author BookingMx Team
 * @version 1.0.0
 * @since 1.0.0
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String m) { super(m); }
}
