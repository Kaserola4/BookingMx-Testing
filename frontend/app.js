/**
 * @fileoverview Main application entry point for BookingMx frontend.
 * 
 * This module initializes the UI, sets up event listeners, and coordinates
 * between the graph module and API module to provide an interactive
 * reservation management and city search experience.
 * 
 * @module app
 * @requires module:graph
 * @requires module:api
 * @author BookingMx Team
 * @version 1.0.0
 */

import { sampleData, validateGraphData, buildGraph, getNearbyCities } from "./js/graph.js";
import { listReservations, createReservation, cancelReservation } from "./js/api.js";

// ==================== Graph UI Setup ====================

/**
 * Form element for nearby city search.
 * @type {HTMLFormElement}
 */
const form = document.getElementById("graph-form");

/**
 * Input element for destination city name.
 * @type {HTMLInputElement}
 */
const destinationEl = document.getElementById("destination");

/**
 * Input element for maximum distance.
 * @type {HTMLInputElement}
 */
const maxDistanceEl = document.getElementById("maxDistance");

/**
 * List element for displaying nearby cities.
 * @type {HTMLUListElement}
 */
const nearbyList = document.getElementById("nearby-list");

/**
 * Validation result for graph data.
 * @type {{ok: boolean, reason?: string}}
 */
const validation = validateGraphData(sampleData);

/**
 * Graph instance for city proximity queries.
 * @type {Graph|null}
 */
const graph = validation.ok ? buildGraph(sampleData.cities, sampleData.edges) : null;

/**
 * Handles form submission for nearby city search.
 * 
 * <p>Queries the graph for cities within the specified distance
 * and updates the UI with results.</p>
 * 
 * @listens form#graph-form~submit
 * @param {Event} e - The form submit event.
 */
form.addEventListener("submit", (e) => {
  e.preventDefault();
  if (!graph) return;
  
  const dest = destinationEl.value.trim();
  const maxD = Number(maxDistanceEl.value);
  const results = getNearbyCities(graph, dest, maxD);
  
  nearbyList.innerHTML = "";
  if (results.length === 0) {
    nearbyList.innerHTML = `<li>No nearby cities found. Check destination or adjust distance.</li>`;
    return;
  }
  
  for (const r of results) {
    const li = document.createElement("li");
    li.textContent = `${r.city} — ${r.distance} km`;
    nearbyList.appendChild(li);
  }
});

// ==================== Reservations UI Setup ====================

/**
 * Form element for creating reservations.
 * @type {HTMLFormElement}
 */
const resForm = document.getElementById("reservation-form");

/**
 * Button element for manually refreshing the reservation list.
 * @type {HTMLButtonElement}
 */
const refreshBtn = document.getElementById("refresh");

/**
 * List element for displaying reservations.
 * @type {HTMLUListElement}
 */
const listEl = document.getElementById("reservation-list");

/**
 * Fetches and displays all reservations from the server.
 * 
 * <p>Updates the reservation list UI with current data.
 * Each reservation includes a cancel button for user interaction.</p>
 * 
 * @async
 * @function refreshReservations
 * @returns {Promise<void>}
 * 
 * @example
 * // Manually refresh reservations
 * await refreshReservations();
 */
async function refreshReservations() {
  listEl.innerHTML = "<li>Loading...</li>";
  try {
    const items = await listReservations();
    listEl.innerHTML = "";
    
    for (const r of items) {
      const li = document.createElement("li");
      li.innerHTML = `
        <strong>#${r.id}</strong> ${r.guestName} @ ${r.hotelName}
        (${r.checkIn} → ${r.checkOut}) [${r.status}]
        <button data-id="${r.id}" class="cancel">Cancel</button>
      `;
      listEl.appendChild(li);
    }
  } catch (e) {
    listEl.innerHTML = `<li>Error: ${e.message}</li>`;
  }
}

/**
 * Handles reservation form submission.
 * 
 * <p>Creates a new reservation using form data and refreshes
 * the reservation list on success.</p>
 * 
 * @listens form#reservation-form~submit
 * @param {Event} e - The form submit event.
 */
resForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  
  const payload = {
    guestName: document.getElementById("guestName").value.trim(),
    hotelName: document.getElementById("hotelName").value.trim(),
    checkIn: document.getElementById("checkIn").value,
    checkOut: document.getElementById("checkOut").value
  };
  
  try {
    await createReservation(payload);
    await refreshReservations();
    resForm.reset();
  } catch (err) {
    alert(err.message);
  }
});

/**
 * Handles cancel button clicks in the reservation list.
 * 
 * <p>Uses event delegation to handle clicks on dynamically
 * created cancel buttons.</p>
 * 
 * @listens ul#reservation-list~click
 * @param {Event} e - The click event.
 */
listEl.addEventListener("click", async (e) => {
  const btn = e.target.closest(".cancel");
  if (!btn) return;
  
  const id = btn.getAttribute("data-id");
  try {
    await cancelReservation(id);
    await refreshReservations();
  } catch (err) {
    alert(err.message);
  }
});

/**
 * Handles manual refresh button click.
 * 
 * @listens button#refresh~click
 */
refreshBtn.addEventListener("click", refreshReservations);

// Initialize reservation list on page load
refreshReservations();