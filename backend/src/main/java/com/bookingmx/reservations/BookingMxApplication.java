package com.bookingmx.reservations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the BookingMx application.
 * <p>
 * This class bootstraps the Spring Boot application and configures
 * component scanning for the entire reservations package.
 * </p>
 * 
 * @author BookingMx Team
 * @version 1.0.0
 * @since 1.0.0
 */
@SpringBootApplication
public class BookingMxApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingMxApplication.class, args);
    }
}
