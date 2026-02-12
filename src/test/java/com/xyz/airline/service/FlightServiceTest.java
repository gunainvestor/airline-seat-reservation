package com.xyz.airline.service;

import com.xyz.airline.domain.Flight;
import com.xyz.airline.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class FlightServiceTest {

    @Test
    void searchFlightsReturnsResults() {
        FlightRepository repo = Mockito.mock(FlightRepository.class);
        FlightService service = new FlightService(repo);

        LocalDate date = LocalDate.of(2026, 2, 12);
        Instant from = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        Flight flight = Flight.builder()
                .id("FL123-2026-02-12")
                .flightNumber("FL123")
                .origin("LHR")
                .destination("CDG")
                .departureTime(from.plusSeconds(3600))
                .build();

        when(repo.findByOriginAndDestinationAndDepartureTimeBetween("LHR", "CDG", from, to))
                .thenReturn(List.of(flight));

        List<Flight> result = service.searchFlights("LHR", "CDG", date);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFlightNumber()).isEqualTo("FL123");
    }
}

