Feature: Cancel reservations
  As a guest
  I want to cancel a reservation
  So I can free my booking if I won’t stay

  Background:
    Given the BookingMx application is running

  Scenario: Successfully cancel a reservation
    Given I am a guest named "Diana"
    And I want to book "Hotel Luna"
    And my check-in date is 1 days from now
    And my check-out date is 2 days from now
    When I create a reservation
    And I cancel the reservation
    Then the reservation status should be "CANCELED"

  Scenario: Fail to cancel a non-existing reservation
    Given I have a non-existing reservation ID
    When I attempt to cancel the reservation
    Then the cancelation should fail with "Reservation not found"
