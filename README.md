# BookingMx

A minimal vanilla JavaScript + Spring Boot project designed to practice unit testing, featuring a hotel reservations management system and a city proximity graph algorithm.

## 📋 Project Overview

BookingMx is a full-stack application that demonstrates best practices in software testing. The project consists of:

- **Backend (Spring Boot)**: RESTful API for managing hotel reservations with comprehensive validation
- **Frontend (Vanilla JavaScript)**: Interactive UI for reservation management and nearby city search functionality
- **Testing Suite**: Extensive unit tests using JUnit 5, Cucumber BDD, and Jest

### Key Features

- ✅ Create, update, cancel, and list hotel reservations
- ✅ Input validation with detailed error messages
- ✅ Graph-based algorithm to find nearby cities within a specified distance
- ✅ 90%+ code coverage on backend services
- ✅ BDD scenarios using Cucumber Gherkin syntax
- ✅ Modern ES Module support in frontend testing

---

## 🚀 Installation Instructions

### Prerequisites

- **Java 21** or higher
- **Maven 3.8+**
- **Node.js 18+** and npm
- Git

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd BookingMx-Testing
   ```

2. **Navigate to backend directory**
   ```bash
   cd backend
   ```

3. **Install dependencies and run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Verify the backend is running**
   ```bash
   curl http://localhost:8080/actuator/health
   # Expected: {"status":"UP"}
   ```

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm run serve
   ```

4. **Access the application**
   
   Open your browser and navigate to: `http://localhost:5173`

---

## 🧪 Testing Documentation

### Backend Tests

The backend uses **JUnit 5** for unit testing and **Cucumber** for BDD (Behavior-Driven Development) tests.

#### Test Structure

```
backend/src/test/java/
├── com/bookingmx/reservations/
│   ├── CucumberTestRunner.java          # Test suite runner
│   ├── steps/
│   │   ├── CucumberTestContextConfiguration.java
│   │   ├── ReservationSteps.java        # BDD step definitions
│   │   ├── ReservationApiSteps.java     # API integration steps
│   │   └── ReservationTest.java         # Unit tests for model
│   └── resources/features/              # Cucumber .feature files
```

#### Running Backend Tests

**Execute all tests with coverage:**
```bash
cd backend
mvn clean test
```

**View coverage report:**
```bash
# After running tests, open:
# target/site/jacoco/index.html
```

#### Test Coverage Requirements

The project enforces **90% line coverage** on all packages (excluding the main application class). The build will fail if coverage falls below this threshold.

#### Example Test Execution Output

```bash
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.bookingmx.reservations.CucumberTestRunner

Scenario: Create a valid reservation                        # PASSED
Scenario: Create reservation with blank guest name          # PASSED
Scenario: Create reservation with invalid dates             # PASSED
Scenario: Update existing reservation                       # PASSED
Scenario: Cancel a reservation                              # PASSED

[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0

[INFO] 
[INFO] --- jacoco:0.8.14:check (check) @ bookingmx-backend ---
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
```

### Frontend Tests

The frontend uses **Jest** for unit testing with ES Module support.

#### Test Structure

```
frontend/js/test/
├── api.test.js          # Tests for API client functions
└── graph.test.js        # Tests for graph algorithms
```

#### Running Frontend Tests

**Execute tests with coverage:**
```bash
cd frontend
npm test
```

#### Example Test Execution Output

```bash
PASS  js/test/api.test.js
  api.js
    listReservations
      ✓ should return reservations on success (3 ms)
      ✓ should throw an error on failure (1 ms)
    createReservation
      ✓ should create a reservation successfully (2 ms)
      ✓ should throw error with message when server returns message (1 ms)

PASS  js/test/graph.test.js
  Graph class
    ✓ should initialize with empty adjacency map (2 ms)
    ✓ addCity adds a valid city (1 ms)
    ✓ addEdge connects two existing cities (1 ms)
  getNearbyCities
    ✓ returns nearby cities within max distance (2 ms)
    ✓ returns sorted nearby cities (1 ms)

--------------------------|---------|----------|---------|---------|
File                      | % Stmts | % Branch | % Funcs | % Lines |
--------------------------|---------|----------|---------|---------|
All files                 |   97.56 |    93.33 |     100 |   97.56 |
 api.js                   |     100 |      100 |     100 |     100 |
 graph.js                 |   96.72 |    90.47 |     100 |   96.72 |
--------------------------|---------|----------|---------|---------|

Test Suites: 2 passed, 2 total
Tests:       20 passed, 20 total
```

---

## 🏗️ Project Architecture

### Backend Architecture

```
com.bookingmx.reservations/
├── controller/          # REST endpoints
├── dto/                 # Data Transfer Objects
├── exception/           # Custom exceptions and handlers
├── model/              # Domain entities
├── repo/               # In-memory repository
└── service/            # Business logic
```

### API Endpoints

| Method | Endpoint                    | Description              |
|--------|----------------------------|--------------------------|
| GET    | `/actuator/health`         | Health check             |
| GET    | `/api/reservations`        | List all reservations    |
| GET    | `/api/reservations/{id}`   | Get reservation by ID    |
| POST   | `/api/reservations`        | Create new reservation   |
| PUT    | `/api/reservations/{id}`   | Update reservation       |
| DELETE | `/api/reservations/{id}`   | Cancel reservation       |

### Frontend Architecture

```
frontend/
├── app.js              # Main application logic
├── js/
│   ├── api.js          # Backend API client
│   └── graph.js        # Graph algorithms
└── index.html          # UI markup
```

---

## 📝 Test Examples

### Example 1: Unit Test (Backend)

**File:** `ReservationTest.java`

```java
@Test
void testEquals_sameInstance() {
    Reservation reservation = new Reservation(1L, "Juan", "Hotel A",
            LocalDate.now(), LocalDate.now().plusDays(1));
    
    assertTrue(reservation.equals(reservation));
}
```

**Expected Result:** ✅ Test passes - object equals itself

### Example 2: BDD Scenario (Backend)

**File:** `reservation-creation.feature`

```gherkin
Scenario: Create a valid reservation
  Given I am a guest named "Juan"
  And I want to book "Paradise Inn"
  And my check-in date is 1 days from now
  And my check-out date is 3 days from now
  When I create a reservation
  Then the reservation should be created successfully
  And the reservation status should be "ACTIVE"
```

**Expected Result:** ✅ Scenario passes - reservation created with ACTIVE status

### Example 3: API Integration Test (Backend)

**File:** `ReservationApiSteps.java`

```java
@When("I POST the request to {string}")
public void iPOSTTheRequestTo(String endpoint) throws Exception {
    mvcResult = mockMvc.perform(post(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andReturn();
}

@Then("the response status should be {int}")
public void theResponseStatusShouldBe(int expectedStatus) {
    assertThat(mvcResult.getResponse().getStatus(), is(expectedStatus));
}
```

**Expected Result:** ✅ POST returns 201 CREATED status

### Example 4: Frontend Unit Test

**File:** `graph.test.js`

```javascript
test("returns sorted nearby cities", () => {
  const g = new Graph();
  g.addCity("A");
  g.addCity("B");
  g.addCity("C");
  g.addEdge("A", "B", 150);
  g.addEdge("A", "C", 100);

  const result = getNearbyCities(g, "A", 200);
  
  expect(result).toEqual([
    { city: "C", distance: 100 },
    { city: "B", distance: 150 }
  ]);
});
```

**Expected Result:** ✅ Returns cities sorted by distance in ascending order

---

## 🛠️ Technologies Used

### Backend
- **Spring Boot 3.5.7** - Application framework
- **Java 21** - Programming language
- **JUnit 5** - Unit testing framework
- **Cucumber 7.18.1** - BDD testing framework
- **JaCoCo 0.8.14** - Code coverage tool
- **Maven** - Build automation

### Frontend
- **Vanilla JavaScript (ES Modules)** - No framework dependencies
- **Jest 30.2.0** - Testing framework
- **http-server** - Development server

---

## 📊 Testing Best Practices Demonstrated

1. **High Code Coverage**: 90%+ line coverage enforced via JaCoCo
2. **BDD Approach**: Business-readable test scenarios using Cucumber
3. **Comprehensive Validation**: Tests for edge cases, null values, and error conditions
4. **API Integration Tests**: Full request/response cycle validation
5. **Pure Functions**: Graph algorithms designed for testability
6. **Mock-based Testing**: Isolated unit tests using Jest mocks
7. **Continuous Validation**: Tests run automatically on build
