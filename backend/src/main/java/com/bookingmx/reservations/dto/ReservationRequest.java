package com.bookingmx.reservations.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Data Transfer Object for reservation creation and update requests.
 * <p>
 * This class represents the input data required to create or update
 * a reservation. All fields are validated using Jakarta Bean Validation
 * annotations to ensure data integrity.
 * </p>
 * 
 * <h3>Validation Rules:</h3>
 * <ul>
 *   <li>Guest name must not be blank</li>
 *   <li>Hotel name must not be blank</li>
 *   <li>Check-in date must be in the future</li>
 *   <li>Check-out date must be in the future</li>
 * </ul>
 * 
 * @author BookingMx Team
 * @version 1.0.0
 * @since 1.0.0
 * @see jakarta.validation.constraints
 */
public class ReservationRequest {

    @NotBlank(message = "Guest name cannot be blank")
    private String guestName;

    @NotBlank(message = "Hotel name cannot be blank")
    private String hotelName;

    @NotNull(message = "Check-in date is required")
    @Future(message = "Check-in date must be in the future")
    private LocalDate checkIn;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOut;

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
}
