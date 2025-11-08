Feature: Reservation API
  As a guest using BookingMx
  I want to manage reservations via the REST API
  So that I can create, update, list, and delete my bookings

  Background:
    Given the BookingMx API is running

  # ==================== CREATE ====================

  Scenario: Successfully create a reservation
    Given I have a valid reservation request
    When I POST the request to "/api/reservations"
    Then the response status should be 201
    And the response should contain a reservation ID
    And the response JSON should include guest name "Juan"
    And the response JSON should include hotel name "Paradise Inn"

  Scenario: Fail to create reservation with missing guest name
    Given I have a reservation request with blank guest name
    When I POST the request to "/api/reservations"
    Then the response status should be 400
    And the response JSON should contain "Guest name cannot be blank"

  Scenario: Fail to create reservation with invalid date range
    Given I have a reservation request with check-out before check-in
    When I POST the request to "/api/reservations"
    Then the response status should be 400
    And the response JSON should contain "Check-out must be after check-in"

  Scenario: Fail to create reservation with missing hotel name
    Given I have a reservation request with blank hotel name
    When I POST the request to "/api/reservations"
    Then the response status should be 400
    And the response JSON should contain "Hotel name cannot be blank"

  # ==================== LIST ====================

  Scenario: List all reservations
    When I GET "/api/reservations"
    Then the response status should be 200
    And the response JSON should be a list

  Scenario: List reservations when none exist
    Given the repository is empty
    When I GET "/api/reservations"
    Then the response status should be 200
    And the response JSON should be an empty list

  # ==================== GET BY ID ====================

  Scenario: Retrieve a reservation by ID
    Given an existing reservation with ID 1
    When I GET "/api/reservations/1"
    Then the response status should be 200
    And the response JSON should include guest name "Juan"

  Scenario: Fail to retrieve a non-existing reservation
    When I GET "/api/reservations/9999"
    Then the response status should be 404
    And the response JSON should contain "Reservation not found"

  # ==================== UPDATE ====================

  Scenario: Successfully update an existing reservation
    Given an existing reservation with ID 2
    And I have a valid update request with new hotel name "Ocean Breeze"
    When I PUT the request to "/api/reservations/2"
    Then the response status should be 200
    And the response JSON should include hotel name "Ocean Breeze"

  Scenario: Fail to update a non-existing reservation
    Given I have a valid update request with new hotel name "Mountain Lodge"
    When I PUT the request to "/api/reservations/9999"
    Then the response status should be 404
    And the response JSON should contain "Reservation not found"

  Scenario: Fail to update with invalid dates
    Given an existing reservation with ID 3
    And I have an update request with check-out before check-in
    When I PUT the request to "/api/reservations/3"
    Then the response status should be 400
    And the response JSON should contain "Check-out must be after check-in"

  # ==================== DELETE ====================

  Scenario: Successfully delete a reservation
    Given an existing reservation with ID 4
    When I DELETE "/api/reservations/4"
    Then the response status should be 204

  Scenario: Fail to delete a non-existing reservation
    When I DELETE "/api/reservations/9999"
    Then the response status should be 404
    And the response JSON should contain "Reservation not found"
