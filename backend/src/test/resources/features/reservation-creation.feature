Feature: Reservation Creation
  As a hotel guest
  I want to create a reservation for my stay
  So that I can secure my accommodation

  Background:
    Given the BookingMx application is running
    And the reservation repository is empty

  Scenario: Successfully create a valid reservation
    Given I am a guest named "John Doe"
    And I want to book "Hotel Marriott"
    And my check-in date is 5 days from now
    And my check-out date is 7 days from now
    When I create a reservation
    Then the reservation should be created successfully
    And the reservation status should be "ACTIVE"
    And the reservation should have a unique ID
    And the reservation should be stored in the system
