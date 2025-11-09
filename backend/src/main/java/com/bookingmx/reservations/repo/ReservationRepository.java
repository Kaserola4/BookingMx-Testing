package com.bookingmx.reservations.repo;

import com.bookingmx.reservations.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory repository for reservation persistence.
 * <p>
 * This repository provides thread-safe storage for reservations using
 * a ConcurrentHashMap. It simulates database behavior with auto-incrementing
 * IDs. Suitable for development and testing purposes.
 * </p>
 * 
 * <h3>Thread Safety:</h3>
 * All operations are thread-safe using concurrent collections.
 * 
 * @author BookingMx Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public class ReservationRepository {
    private final Map<Long, Reservation> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1L);

    public List<Reservation> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Reservation save(Reservation r) {
        if (r.getId() == null) r.setId(seq.getAndIncrement());
        store.put(r.getId(), r);
        return r;
    }

    public void deleteAll() {
        store.clear();
        seq.set(1L);
    }
}
