/**
 * @fileoverview API client module for BookingMx backend communication.
 * 
 * This module provides a simple, Promise-based interface for interacting
 * with the BookingMx reservation API. All functions are designed to be
 * easily mockable for testing purposes.
 * 
 * @module api
 * @author BookingMx Team
 * @version 1.0.0
 */

/**
 * Base URL for the BookingMx API endpoints.
 * @constant {string}
 * @default
 */
const BASE_URL = "http://localhost:8080/api/reservations";

/**
 * Fetches all reservations from the server.
 * 
 * @async
 * @function listReservations
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of reservation objects.
 * @throws {Error} If the server returns a non-OK response.
 * 
 * @example
 * try {
 *   const reservations = await listReservations();
 *   console.log(`Found ${reservations.length} reservations`);
 * } catch (error) {
 *   console.error('Failed to fetch reservations:', error.message);
 * }
 */
export async function listReservations() {
  const res = await fetch(BASE_URL);
  if (!res.ok) throw new Error("Failed to fetch reservations");
  return res.json();
}

/**
 * Creates a new reservation on the server.
 * 
 * @async
 * @function createReservation
 * @param {Object} payload - The reservation data to create.
 * @param {string} payload.guestName - The name of the guest.
 * @param {string} payload.hotelName - The name of the hotel.
 * @param {string} payload.checkIn - Check-in date in ISO format (YYYY-MM-DD).
 * @param {string} payload.checkOut - Check-out date in ISO format (YYYY-MM-DD).
 * @returns {Promise<Object>} A promise that resolves to the created reservation object with assigned ID.
 * @throws {Error} If the server returns an error or validation fails.
 * 
 * @example
 * const newReservation = {
 *   guestName: "Juan Pérez",
 *   hotelName: "Paradise Inn",
 *   checkIn: "2025-12-01",
 *   checkOut: "2025-12-05"
 * };
 * 
 * try {
 *   const created = await createReservation(newReservation);
 *   console.log(`Reservation created with ID: ${created.id}`);
 * } catch (error) {
 *   console.error('Creation failed:', error.message);
 * }
 */
export async function createReservation(payload) {
  const res = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  if (!res.ok) throw new Error((await res.json()).message || "Create failed");
  return res.json();
}

/**
 * Updates an existing reservation on the server.
 * 
 * @async
 * @function updateReservation
 * @param {number|string} id - The unique identifier of the reservation to update.
 * @param {Object} payload - The updated reservation data.
 * @param {string} payload.guestName - The updated guest name.
 * @param {string} payload.hotelName - The updated hotel name.
 * @param {string} payload.checkIn - Updated check-in date in ISO format.
 * @param {string} payload.checkOut - Updated check-out date in ISO format.
 * @returns {Promise<Object>} A promise that resolves to the updated reservation object.
 * @throws {Error} If the reservation doesn't exist, is canceled, or validation fails.
 * 
 * @example
 * try {
 *   const updated = await updateReservation(5, {
 *     guestName: "Juan Pérez",
 *     hotelName: "Grand Hotel",
 *     checkIn: "2025-12-10",
 *     checkOut: "2025-12-15"
 *   });
 *   console.log('Reservation updated successfully');
 * } catch (error) {
 *   console.error('Update failed:', error.message);
 * }
 */
export async function updateReservation(id, payload) {
  const res = await fetch(`${BASE_URL}/${encodeURIComponent(id)}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  if (!res.ok) throw new Error((await res.json()).message || "Update failed");
  return res.json();
}

/**
 * Cancels an existing reservation.
 * 
 * <p><strong>Note:</strong> This operation marks the reservation as canceled
 * but does not delete it from the system.</p>
 * 
 * @async
 * @function cancelReservation
 * @param {number|string} id - The unique identifier of the reservation to cancel.
 * @returns {Promise<Object>} A promise that resolves to the server response.
 * @throws {Error} If the reservation doesn't exist or the operation fails.
 * 
 * @example
 * try {
 *   await cancelReservation(5);
 *   console.log('Reservation canceled successfully');
 * } catch (error) {
 *   console.error('Cancellation failed:', error.message);
 * }
 */
export async function cancelReservation(id) {
  const res = await fetch(`${BASE_URL}/${encodeURIComponent(id)}`, { 
    method: "DELETE" 
  });
  if (!res.ok) throw new Error((await res.json()).message || "Cancel failed");
  return res.json();
}