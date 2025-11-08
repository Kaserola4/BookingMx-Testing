package com.bookingmx.reservations.steps;

import com.bookingmx.reservations.BookingMxApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = BookingMxApplication.class)
public class CucumberTestContextConfiguration {
}
