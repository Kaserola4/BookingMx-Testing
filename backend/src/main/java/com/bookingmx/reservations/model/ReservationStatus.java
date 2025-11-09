package com.bookingmx.reservations.model;

/**
 * Enumeration of possible reservation statuses.
 * <p>
 * A reservation can be in one of two states:
 * <ul>
 *   <li><b>ACTIVE</b> - The reservation is confirmed and active</li>
 *   <li><b>CANCELED</b> - The reservation has been canceled</li>
 * </ul>
 * </p>
 * 
 * @author BookingMx Team
 * @version 1.0.0
 * @since 1.0.0
 */
public enum ReservationStatus {
    ACTIVE, CANCELED
}
