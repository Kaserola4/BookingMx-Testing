package com.bookingmx.reservations.service;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.model.ReservationStatus;
import com.bookingmx.reservations.repo.ReservationRepository;
import com.bookingmx.reservations.exception.BadRequestException;
import com.bookingmx.reservations.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing reservation business logic.
 * <p>
 * This service handles all business operations related to reservations
 * including creation, updates, cancellation, and retrieval. It enforces
 * business rules such as date validation and status checks.
 * </p>
 * 
 * <h3>Business Rules:</h3>
 * <ul>
 *   <li>Check-in and check-out dates must be in the future</li>
 *   <li>Check-out must be after check-in</li>
 *   <li>Canceled reservations cannot be updated</li>
 *   <li>All dates must be non-null</li>
 * </ul>
 * 
 * @author BookingMx Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class ReservationService {
    private final ReservationRepository repo;

    public ReservationService(ReservationRepository repo) {
        this.repo = repo;
    }

    public List<Reservation> list() {
        return repo.findAll();
    }

    public Optional<Reservation> getById(Long id) {
        return repo.findById(id);
    }


    public Reservation create(ReservationRequest req) {
        validateDates(req.getCheckIn(), req.getCheckOut());
        Reservation r = new Reservation(null, req.getGuestName(), req.getHotelName(), req.getCheckIn(), req.getCheckOut());
        return repo.save(r);
    }

    public Reservation update(Long id, ReservationRequest req) {
        Reservation existing = repo.findById(id).orElseThrow(() -> new NotFoundException("Reservation not found"));
        if (!existing.isActive()) throw new BadRequestException("Cannot update a canceled reservation");
        validateDates(req.getCheckIn(), req.getCheckOut());
        existing.setGuestName(req.getGuestName());
        existing.setHotelName(req.getHotelName());
        existing.setCheckIn(req.getCheckIn());
        existing.setCheckOut(req.getCheckOut());
        return repo.save(existing);
    }

    public Reservation cancel(Long id) {
        Reservation existing = repo.findById(id).orElseThrow(() -> new NotFoundException("Reservation not found"));
        existing.setStatus(ReservationStatus.CANCELED);
        return repo.save(existing);
    }

    private void validateDates(LocalDate in, LocalDate out) {
        if (in == null || out == null) throw new BadRequestException("Dates cannot be null");
        if (in.isBefore(LocalDate.now())) throw new BadRequestException("Check-in must be in the future");
        if (out.isBefore(LocalDate.now())) throw new BadRequestException("Check-out must be in the future");
        if (!out.isAfter(in)) throw new BadRequestException("Check-out must be after check-in");
    }
}
