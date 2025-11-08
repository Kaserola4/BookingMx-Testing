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

  Scenario: Create reservation with multiple nights
    Given I am a guest named "Jane Smith"
    And I want to book "Hilton Garden Inn"
    And my check-in date is 10 days from now
    And my check-out date is 15 days from now
    When I create a reservation
    Then the reservation should be created successfully
    And the stay duration should be 5 nights

  Scenario: Fail to create reservation with null check-in date
    Given I am a guest named "Bob Wilson"
    And I want to book "Holiday Inn"
    And my check-in date is null
    And my check-out date is 5 days from now
    When I attempt to create a reservation
    Then the creation should fail
    And I should receive an error message "Dates cannot be null"
    And no reservation should be stored

  Scenario: Fail to create reservation with null check-out date
    Given I am a guest named "Alice Brown"
    And I want to book "Best Western"
    And my check-in date is 3 days from now
    And my check-out date is null
    When I attempt to create a reservation
    Then the creation should fail
    And I should receive an error message "Dates cannot be null"

  Scenario: Fail to create reservation with check-out before check-in
    Given I am a guest named "Charlie Davis"
    And I want to book "Sheraton Hotel"
    And my check-in date is 7 days from now
    And my check-out date is 5 days from now
    When I attempt to create a reservation
    Then the creation should fail
    And I should receive an error message "Check-out must be after check-in"

  Scenario: Fail to create reservation with same check-in and check-out dates
    Given I am a guest named "Diana Evans"
    And I want to book "Radisson Hotel"
    And my check-in date is 4 days from now
    And my check-out date is 4 days from now
    When I attempt to create a reservation
    Then the creation should fail
    And I should receive an error message "Check-out must be after check-in"

  Scenario: Fail to create reservation with check-in date in the past
    Given I am a guest named "Frank Miller"
    And I want to book "Crowne Plaza"
    And my check-in date is 2 days ago
    And my check-out date is 5 days from now
    When I attempt to create the reservation
    Then the creation should fail
    And I should receive an error message "Check-in must be in the future"

  Scenario: Fail to create reservation with check-out date in the past
    Given I am a guest named "Grace Taylor"
    And I want to book "InterContinental"
    And my check-in date is 5 days from now
    And my check-out date is yesterday
    When I attempt to create the reservation
    Then the creation should fail
    And I should receive an error message "Check-out must be in the future"

  Scenario: Accept reservation with minimum valid duration (1 night)
    Given I am a guest named "Short Stayer"
    And I want to book "Quick Stop Inn"
    And my check-in date is tomorrow
    And my check-out date is 2 days from now
    When I create a reservation
    Then the creation should be successful
    And the stay duration should be 1 nights

  Scenario: Accept reservation with extended duration (30 nights)
    Given I am a guest named "Long Stayer"
    And I want to book "Extended Stay"
    And my check-in date is 10 days from now
    And my check-out date is 40 days from now
    When I create a reservation
    Then the creation should be successful
    And the stay duration should be 30 nights