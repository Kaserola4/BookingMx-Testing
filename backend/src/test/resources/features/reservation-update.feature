Feature: Update reservations
  As a guest
  I want to update my booking details
  So I can modify my reservation dates or hotel name

  Background:
    Given the BookingMx application is running

  Scenario: Successfully update an existing reservation
    Given I am a guest named "Carlos"
    And I want to book "Hotel Playa"
    And my check-in date is 1 days from now
    And my check-out date is 3 days from now
    When I create a reservation
    And I update the reservation hotel to "Hotel Sierra"
    Then the update should be successful
    And the hotel name should be "Hotel Sierra"

  Scenario: Fail to update a non-existing reservation
    Given I prepare a reservation update for a non-existing ID
    When I attempt to update the reservation
    Then the update should fail with "Reservation not found"
