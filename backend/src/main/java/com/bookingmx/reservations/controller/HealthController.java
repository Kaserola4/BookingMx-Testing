package com.bookingmx.reservations.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Controller for application health checks.
 * <p>
 * Provides a simple endpoint to verify that the application is running
 * and responsive. Used by monitoring tools and deployment pipelines.
 * </p>
 * 
 * @author BookingMx Team
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/actuator")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}