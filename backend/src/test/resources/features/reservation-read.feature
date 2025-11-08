Feature: Read reservations
  As a hotel staff member
  I want to view reservations
  So I can verify that bookings are stored correctly

  Background:
    Given the BookingMx application is running

  Scenario: List all reservations
    Given I am a guest named "Alice"
    And I want to book "Hotel Azul"
    And my check-in date is 2 days from now
    And my check-out date is 4 days from now
    When I create a reservation
    Then the reservation should be created successfully
    When I list all reservations
    Then I should see at least one reservation

  Scenario: Get reservation by ID
    Given I am a guest named "Bob"
    And I want to book "Hotel Sol"
    And my check-in date is 3 days from now
    And my check-out date is 5 days from now
    When I create a reservation
    And I get the reservation by ID
    Then the retrieved reservation should match the created one
