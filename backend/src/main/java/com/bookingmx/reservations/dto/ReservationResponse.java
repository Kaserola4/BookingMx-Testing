package com.bookingmx.reservations.dto;

import com.bookingmx.reservations.model.ReservationStatus;
import java.time.LocalDate;

/**
 * Data Transfer Object for reservation responses.
 * <p>
 * This immutable class represents the data returned to clients
 * when querying reservations. It includes all reservation details
 * including the unique identifier and current status.
 * </p>
 * 
 * @author BookingMx Team
 * @version 1.0.0
 * @since 1.0.0
 */
public class ReservationResponse {
    private Long id;
    private String guestName;
    private String hotelName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationStatus status;

    public ReservationResponse(Long id, String guestName, String hotelName, LocalDate checkIn, LocalDate checkOut, ReservationStatus status) {
        this.id = id; this.guestName = guestName; this.hotelName = hotelName;
        this.checkIn = checkIn; this.checkOut = checkOut; this.status = status;
    }

    public Long getId() { return id; }
    public String getGuestName() { return guestName; }
    public String getHotelName() { return hotelName; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public ReservationStatus getStatus() { return status; }
}
