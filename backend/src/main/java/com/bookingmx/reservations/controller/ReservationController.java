package com.bookingmx.reservations.controller;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.dto.ReservationResponse;
import com.bookingmx.reservations.exception.NotFoundException;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.service.ReservationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for reservation management endpoints.
 * <p>
 * This controller exposes HTTP endpoints for creating, reading, updating,
 * and deleting hotel reservations. All endpoints return JSON responses
 * and support CORS for frontend integration.
 * </p>
 * 
 * <h3>Endpoints:</h3>
 * <ul>
 *   <li><b>GET /api/reservations</b> - List all reservations</li>
 *   <li><b>GET /api/reservations/{id}</b> - Get reservation by ID</li>
 *   <li><b>POST /api/reservations</b> - Create new reservation</li>
 *   <li><b>PUT /api/reservations/{id}</b> - Update reservation</li>
 *   <li><b>DELETE /api/reservations/{id}</b> - Cancel reservation</li>
 * </ul>
 * 
 * @author BookingMx Team
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "*"})
@RequestMapping(value = "/api/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservationResponse> list() {
        return service.list().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ReservationResponse getById(@PathVariable("id") Long id) {
        return service.getById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@Valid @RequestBody ReservationRequest req) {
        return toResponse(service.create(req));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReservationResponse update(@PathVariable("id") Long id, @Valid @RequestBody ReservationRequest req) {
        return toResponse(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable("id") Long id) {
        service.cancel(id);
    }

    private ReservationResponse toResponse(Reservation r) {
        return new ReservationResponse(
                r.getId(), r.getGuestName(), r.getHotelName(), r.getCheckIn(), r.getCheckOut(), r.getStatus()
        );
    }
}
