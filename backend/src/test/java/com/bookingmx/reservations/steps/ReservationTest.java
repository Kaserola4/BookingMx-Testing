package com.bookingmx.reservations.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void testEquals_sameInstance() {
        // Given
        Reservation reservation = new Reservation(1L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));

        // When & Then - same object should equal itself (hits: if (this == o) return true)
        assertTrue(reservation.equals(reservation));
    }

    @Test
    void testEquals_equalReservations() {
        // Given
        Reservation r1 = new Reservation(1L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));
        Reservation r2 = new Reservation(1L, "Maria", "Hotel B",
                LocalDate.now(), LocalDate.now().plusDays(2));

        // When & Then - reservations with same ID should be equal (Objects.equals returns true)
        assertTrue(r1.equals(r2));
        assertEquals(r1, r2);
    }

    @Test
    void testEquals_differentIds() {
        // Given
        Reservation r1 = new Reservation(1L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));
        Reservation r2 = new Reservation(2L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));

        // When & Then - reservations with different IDs should not be equal (Objects.equals returns false)
        assertFalse(r1.equals(r2));
        assertNotEquals(r1, r2);
    }

    @Test
    void testEquals_nullId() {
        // Given
        Reservation r1 = new Reservation(null, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));
        Reservation r2 = new Reservation(null, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));

        // When & Then - reservations with both null IDs should be equal
        assertTrue(r1.equals(r2));
        assertEquals(r1, r2);
    }

    @Test
    void testEquals_oneNullId() {
        // Given
        Reservation r1 = new Reservation(1L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));
        Reservation r2 = new Reservation(null, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));

        // When & Then - one null, one non-null ID should not be equal
        assertFalse(r1.equals(r2));
        assertNotEquals(r1, r2);
    }

    @Test
    void testEquals_withNull() {
        // Given
        Reservation reservation = new Reservation(1L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));

        // When & Then - comparing with null (hits instanceof check)
        assertFalse(reservation.equals(null));
        assertNotEquals(reservation, null);
    }

    @Test
    void testEquals_withDifferentClass() {
        // Given
        Reservation reservation = new Reservation(1L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));
        String notAReservation = "Not a reservation";

        // When & Then - comparing with different type (hits instanceof check)
        assertFalse(reservation.equals(notAReservation));
        assertNotEquals(reservation, notAReservation);
    }

    @Test
    void testHashCode_sameId() {
        // Given
        Reservation r1 = new Reservation(1L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));
        Reservation r2 = new Reservation(1L, "Maria", "Hotel B",
                LocalDate.now(), LocalDate.now().plusDays(2));

        // When & Then - equal objects must have same hash code
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testHashCode_differentIds() {
        // Given
        Reservation r1 = new Reservation(1L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));
        Reservation r2 = new Reservation(2L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));

        // When & Then - different IDs should likely have different hash codes
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testIsActive_whenActive() {
        // Given
        Reservation reservation = new Reservation(1L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));

        // When & Then
        assertTrue(reservation.isActive());
        assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());
    }

    @Test
    void testIsActive_whenCanceled() {
        // Given
        Reservation reservation = new Reservation(1L, "Juan", "Hotel A",
                LocalDate.now(), LocalDate.now().plusDays(1));
        reservation.setStatus(ReservationStatus.CANCELED);

        // When & Then
        assertFalse(reservation.isActive());
        assertEquals(ReservationStatus.CANCELED, reservation.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        Reservation reservation = new Reservation(1L, "Juan", "Hotel A",
                LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 5));

        // When
        reservation.setId(2L);
        reservation.setGuestName("Maria");
        reservation.setHotelName("Hotel B");
        reservation.setCheckIn(LocalDate.of(2025, 12, 1));
        reservation.setCheckOut(LocalDate.of(2025, 12, 5));
        reservation.setStatus(ReservationStatus.CANCELED);

        // Then
        assertEquals(2L, reservation.getId());
        assertEquals("Maria", reservation.getGuestName());
        assertEquals("Hotel B", reservation.getHotelName());
        assertEquals(LocalDate.of(2025, 12, 1), reservation.getCheckIn());
        assertEquals(LocalDate.of(2025, 12, 5), reservation.getCheckOut());
        assertEquals(ReservationStatus.CANCELED, reservation.getStatus());
    }
}