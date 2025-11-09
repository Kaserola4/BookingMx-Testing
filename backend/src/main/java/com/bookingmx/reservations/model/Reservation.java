package com.bookingmx.reservations.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a hotel reservation in the BookingMx system.
 * <p>
 * This class encapsulates all information about a reservation including
 * guest details, hotel information, check-in/check-out dates, and status.
 * Reservations are uniquely identified by their ID and can be in either
 * ACTIVE or CANCELED status.
 * </p>
 * 
 * @author BookingMx Team
 * @version 1.0.0
 * @since 1.0.0
 */
public class Reservation {
    private Long id;
    private String guestName;
    private String hotelName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationStatus status = ReservationStatus.ACTIVE;

    /**
     * Constructs a new Reservation with the specified details.
     * 
     * @param id         the unique identifier for this reservation
     * @param guestName  the name of the guest
     * @param hotelName  the name of the hotel
     * @param checkIn    the check-in date
     * @param checkOut   the check-out date
     */
    public Reservation(Long id, String guestName, String hotelName, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.guestName = guestName;
        this.hotelName = hotelName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    /**
     * Checks if this reservation is currently active.
     * 
     * @return {@code true} if status is ACTIVE, {@code false} otherwise
     */
    public boolean isActive() { return this.status == ReservationStatus.ACTIVE; }
    
    /**
     * Compares this reservation to another object for equality.
     * Two reservations are considered equal if they have the same ID.
     * 
     * @param o the object to compare with
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
