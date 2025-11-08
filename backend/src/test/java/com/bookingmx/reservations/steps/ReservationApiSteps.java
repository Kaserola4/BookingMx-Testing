package com.bookingmx.reservations.steps;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.repo.ReservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ReservationApiSteps extends CucumberTestContextConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ReservationRequest request;
    private MvcResult mvcResult;

    // Store created reservation IDs - maps scenario ID to actual database ID
    private Map<Integer, Long> createdReservationIds = new HashMap<>();

    @Autowired
    private ReservationRepository repository;

    @Before
    public void setup() {
        request = new ReservationRequest();
        mvcResult = null;
        createdReservationIds.clear();
    }

    // ==================== GIVEN ====================

    @Given("the BookingMx API is running")
    public void theApiIsRunning() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Given("I have a valid reservation request")
    public void iHaveAValidReservationRequest() {
        request.setGuestName("Juan");
        request.setHotelName("Paradise Inn");
        request.setCheckIn(LocalDate.now().plusDays(1));
        request.setCheckOut(LocalDate.now().plusDays(3));
    }

    @Given("I have a reservation request with blank guest name")
    public void iHaveAReservationRequestWithBlankGuestName() {
        request.setGuestName("");
        request.setHotelName("Paradise Inn");
        request.setCheckIn(LocalDate.now().plusDays(1));
        request.setCheckOut(LocalDate.now().plusDays(3));
    }

    @Given("I have a reservation request with blank hotel name")
    public void iHaveAReservationRequestWithBlankHotelName() {
        request.setGuestName("Juan");
        request.setHotelName("");
        request.setCheckIn(LocalDate.now().plusDays(1));
        request.setCheckOut(LocalDate.now().plusDays(3));
    }

    @Given("I have a reservation request with check-out before check-in")
    public void iHaveAReservationRequestWithInvalidDates() {
        request.setGuestName("Juan");
        request.setHotelName("Paradise Inn");
        request.setCheckIn(LocalDate.now().plusDays(5));
        request.setCheckOut(LocalDate.now().plusDays(3));
    }

    @Given("the repository is empty")
    public void theRepositoryIsEmpty() {
        repository.deleteAll();
        createdReservationIds.clear();
    }

    @Given("an existing reservation with ID {int}")
    public void anExistingReservationWithId(int expectedId) throws Exception {
        ReservationRequest newRequest = new ReservationRequest();
        newRequest.setGuestName("Juan");
        newRequest.setHotelName("Default Hotel");
        newRequest.setCheckIn(LocalDate.now().plusDays(1));
        newRequest.setCheckOut(LocalDate.now().plusDays(2));

        MvcResult result = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        System.out.println("Created reservation response: " + responseJson);

        // Extract the actual ID from response
        Map<String, Object> response = objectMapper.readValue(responseJson, Map.class);
        Long actualId = ((Number) response.get("id")).longValue();

        // Store the mapping between expected (scenario) ID and actual (database) ID
        createdReservationIds.put(expectedId, actualId);

        System.out.println("Mapped scenario ID " + expectedId + " to database ID " + actualId);
    }

    @Given("I have a valid update request with new hotel name {string}")
    public void iHaveAValidUpdateRequestWithNewHotelName(String hotelName) {
        request.setGuestName("Juan");
        request.setHotelName(hotelName);
        request.setCheckIn(LocalDate.now().plusDays(1));
        request.setCheckOut(LocalDate.now().plusDays(2));
    }

    @Given("I have an update request with check-out before check-in")
    public void iHaveAnUpdateRequestWithInvalidDates() {
        request.setGuestName("Juan");
        request.setHotelName("Paradise Inn");
        request.setCheckIn(LocalDate.now().plusDays(3));
        request.setCheckOut(LocalDate.now().plusDays(1));
    }

    // ==================== WHEN ====================

    @When("I POST the request to {string}")
    public void iPOSTTheRequestTo(String endpoint) throws Exception {
        mvcResult = mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
    }

    @When("I GET {string}")
    public void iGET(String endpoint) throws Exception {
        // Replace scenario ID in endpoint with actual database ID
        String actualEndpoint = replaceIdInEndpoint(endpoint);

        try {
            mvcResult = mockMvc.perform(get(actualEndpoint))
                    .andReturn();

            System.out.println("GET " + actualEndpoint + " - Status: " + mvcResult.getResponse().getStatus());
            System.out.println("Response body: " + mvcResult.getResponse().getContentAsString());
            if (mvcResult.getResponse().getStatus() >= 400) {
                System.out.println("Error message: " + mvcResult.getResponse().getErrorMessage());
            }
        } catch (Exception e) {
            System.err.println("Exception during GET " + actualEndpoint + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @When("I PUT the request to {string}")
    public void iPUTTheRequestTo(String endpoint) throws Exception {
        String actualEndpoint = replaceIdInEndpoint(endpoint);

        mvcResult = mockMvc.perform(put(actualEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
    }

    @When("I DELETE {string}")
    public void iDELETE(String endpoint) throws Exception {
        String actualEndpoint = replaceIdInEndpoint(endpoint);

        mvcResult = mockMvc.perform(delete(actualEndpoint))
                .andReturn();
    }

    // ==================== THEN ====================

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) throws Exception {
        assertThat(mvcResult.getResponse().getStatus(), is(expectedStatus));
    }

    @Then("the response should contain a reservation ID")
    public void theResponseShouldContainAReservationID() throws Exception {
        String json = mvcResult.getResponse().getContentAsString();
        assertThat(json, containsString("id"));
    }

    @Then("the response JSON should include guest name {string}")
    public void theResponseJSONShouldIncludeGuestName(String name) throws Exception {
        String json = mvcResult.getResponse().getContentAsString();
        assertThat(json, containsString("\"guestName\":\"" + name + "\""));
    }

    @Then("the response JSON should include hotel name {string}")
    public void theResponseJSONShouldIncludeHotelName(String hotel) throws Exception {
        String json = mvcResult.getResponse().getContentAsString();
        assertThat(json, containsString("\"hotelName\":\"" + hotel + "\""));
    }

    @Then("the response JSON should contain {string}")
    public void theResponseJSONShouldContain(String expectedText) throws Exception {
        String json = mvcResult.getResponse().getContentAsString();
        assertThat(json, containsString(expectedText));
    }

    @Then("the response JSON should be a list")
    public void theResponseJSONShouldBeAList() throws Exception {
        String json = mvcResult.getResponse().getContentAsString();
        assertThat(json.trim(), startsWith("["));
    }

    @Then("the response JSON should be an empty list")
    public void theResponseJSONShouldBeAnEmptyList() throws Exception {
        String json = mvcResult.getResponse().getContentAsString();
        assertThat(json.trim(), equalTo("[]"));
    }

    // ==================== HELPER METHODS ====================

    /**
     * Replaces scenario IDs in endpoint paths with actual database IDs.
     * For example, "/api/reservations/3" becomes "/api/reservations/1"
     * if scenario ID 3 maps to database ID 1.
     */
    private String replaceIdInEndpoint(String endpoint) {
        for (Map.Entry<Integer, Long> entry : createdReservationIds.entrySet()) {
            String scenarioIdStr = "/" + entry.getKey();
            String databaseIdStr = "/" + entry.getValue();
            if (endpoint.contains(scenarioIdStr)) {
                String replaced = endpoint.replace(scenarioIdStr, databaseIdStr);
                System.out.println("Replaced endpoint: " + endpoint + " -> " + replaced);
                return replaced;
            }
        }
        return endpoint;
    }
}