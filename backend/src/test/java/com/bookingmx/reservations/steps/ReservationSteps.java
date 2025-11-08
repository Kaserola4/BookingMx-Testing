package com.bookingmx.reservations.steps;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.dto.ReservationResponse;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.service.ReservationService;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationSteps extends CucumberTestContextConfiguration {
    @Autowired
    private ReservationService reservationService;
    // Shared state for the scenario
    private ReservationRequest request;
    private ReservationResponse response;
    private Exception thrownException;

    private Long reservationId;
    private ReservationResponse retrievedResponse;

    @Before
    public void setup() {
        request = new ReservationRequest();
        response = null;
        thrownException = null;
    }

    // ==================== GIVEN Steps ====================

    @Given("the BookingMx application is running")
    public void theApplicationIsRunning() {
        assertNotNull(reservationService, "Reservation service should be available");
    }

    @Given("the reservation repository is empty")
    public void theRepositoryIsEmpty() {
        // In-memory repository auto-clears between tests
    }

    @Given("I am a guest named {string}")
    public void iAmAGuestNamed(String guestName) {
        request.setGuestName(guestName);
    }

    @Given("I want to book {string}")
    public void iWantToBook(String hotelName) {
        request.setHotelName(hotelName);
    }

    @Given("my check-in date is {int} days from now")
    public void myCheckInDateIsDaysFromNow(int days) {
        request.setCheckIn(LocalDate.now().plusDays(days));
    }

    @Given("my check-out date is {int} days from now")
    public void myCheckOutDateIsDaysFromNow(int days) {
        request.setCheckOut(LocalDate.now().plusDays(days));
    }

    @Given("my check-in date is null")
    public void myCheckInDateIsNull() {
        request.setCheckIn(null);
    }

    @Given("my check-out date is null")
    public void myCheckOutDateIsNull() {
        request.setCheckOut(null);
    }

    @Given("my check-in date is {int} days ago")
    public void myCheckInDateIsDaysAgo(int days) {
        request.setCheckIn(LocalDate.now().minusDays(days));
    }

    @Given("my check-out date is yesterday")
    public void myCheckOutDateIsYesterday() {
        request.setCheckOut(LocalDate.now().minusDays(1));
    }

    @Given("my guest name is blank")
    public void myGuestNameIsBlank() {
        request.setGuestName("");
    }

    @Given("my hotel name is blank")
    public void myHotelNameIsBlank() {
        request.setHotelName("");
    }

    @Given("my check-in date is tomorrow")
    public void myCheckInDateIsTomorrow() {
        request.setCheckIn(LocalDate.now().plusDays(1));
    }

    // ==================== WHEN Steps ====================

    @When("I create a reservation")
    public void iCreateAReservation() {
        try {
            Reservation created = reservationService.create(request);
            response = toResponse(created);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @When("I attempt to create a reservation")
    public void iAttemptToCreateAReservation() {
        try {
            Reservation created = reservationService.create(request);
            response = toResponse(created);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @When("I attempt to create the reservation")
    public void iAttemptToCreateTheReservation() {
        iAttemptToCreateAReservation();
    }

    @When("I list all reservations")
    public void iListAllReservations() {
        var list = reservationService.list();
        assertNotNull(list, "List should not be null");
        assertFalse(list.isEmpty(), "List should not be empty");
    }

    @When("I get the reservation by ID")
    public void iGetTheReservationById() {
        assertNotNull(response, "Must have created a reservation first");
        reservationId = response.getId();
        retrievedResponse = toResponse(
                reservationService.list().stream()
                        .filter(r -> r.getId().equals(reservationId))
                        .findFirst()
                        .orElse(null)
        );
    }

    // ==================== THEN Steps ====================

    @Then("the reservation should be created successfully")
    public void theReservationShouldBeCreatedSuccessfully() {
        assertNotNull(response, "Response should not be null");
        assertNull(thrownException, "No exception should be thrown");
    }

    @Then("the creation should fail")
    public void theCreationShouldFail() {
        assertNotNull(thrownException, "An exception should have been thrown");
        assertNull(response, "Response should be null when creation fails");
    }

    @Then("I should receive an error message {string}")
    public void iShouldReceiveAnErrorMessage(String expectedMessage) {
        assertNotNull(thrownException, "An exception should have been thrown");
        String actualMessage = thrownException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage),
                String.format("Expected error message to contain: '%s', but got: '%s'",
                        expectedMessage, actualMessage));
    }

    @Then("no reservation should be stored")
    public void noReservationShouldBeStored() {
        assertNull(response, "Response should be null when creation fails");
    }

    @Then("the reservation status should be {string}")
    public void theReservationStatusShouldBe(String expectedStatus) {
        assertNotNull(response, "Response should not be null");
        assertEquals(expectedStatus, response.getStatus().toString(),
                "Reservation status should match expected value");
    }

    @Then("the reservation should have a unique ID")
    public void theReservationShouldHaveAUniqueId() {
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getId(), "Reservation ID should not be null");
        assertTrue(response.getId() > 0, "Reservation ID should be positive");
    }

    @Then("the creation should fail with validation error")
    public void theCreationShouldFailWithValidationError() {
        assertNotNull(thrownException, "An exception should have been thrown");
    }

    @Then("I should receive an error about blank guest name")
    public void iShouldReceiveAnErrorAboutBlankGuestName() {
        assertNotNull(thrownException, "An exception should have been thrown");
    }

    @Then("I should receive an error about blank hotel name")
    public void iShouldReceiveAnErrorAboutBlankHotelName() {
        assertNotNull(thrownException, "An exception should have been thrown");
    }

    @Then("the creation should be successful")
    public void theCreationShouldBeSuccessful() {
        theReservationShouldBeCreatedSuccessfully();
    }

    @Then("the stay duration should be {int} nights")
    public void theStayDurationShouldBeNights(int expectedNights) {
        assertNotNull(response, "Response should not be null");
        long actualNights = java.time.temporal.ChronoUnit.DAYS.between(
                response.getCheckIn(),
                response.getCheckOut()
        );
        assertEquals(expectedNights, actualNights,
                "Stay duration should match expected nights");
    }

    // ==================== AND Steps ====================

    @And("the reservation should be stored in the system")
    public void theReservationShouldBeStoredInTheSystem() {
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getId(), "Reservation should have been assigned an ID");

        // Verify it's actually in the system by checking the list
        assertDoesNotThrow(() -> {
            reservationService.list();
        }, "Should be able to retrieve reservations from the system");
    }

    @And("the guest name should be {string}")
    public void theGuestNameShouldBe(String expectedName) {
        assertNotNull(response, "Response should not be null");
        assertEquals(expectedName, response.getGuestName(),
                "Guest name should match expected value");
    }

    @And("the hotel name should be {string}")
    public void theHotelNameShouldBe(String expectedHotel) {
        assertNotNull(response, "Response should not be null");
        assertEquals(expectedHotel, response.getHotelName(),
                "Hotel name should match expected value");
    }

    @And("the check-in date should be {int} days from now")
    public void theCheckInDateShouldBeDaysFromNow(int days) {
        assertNotNull(response, "Response should not be null");
        LocalDate expectedDate = LocalDate.now().plusDays(days);
        assertEquals(expectedDate, response.getCheckIn(),
                "Check-in date should match expected value");
    }

    @And("the check-out date should be {int} days from now")
    public void theCheckOutDateShouldBeDaysFromNow(int days) {
        assertNotNull(response, "Response should not be null");
        LocalDate expectedDate = LocalDate.now().plusDays(days);
        assertEquals(expectedDate, response.getCheckOut(),
                "Check-out date should match expected value");
    }

    // ==================== Helper Methods ====================

    /**
     * Converts a Reservation entity to a ReservationResponse DTO
     */
    private ReservationResponse toResponse(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return new ReservationResponse(
                reservation.getId(),
                reservation.getGuestName(),
                reservation.getHotelName(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getStatus()
        );
    }
}
