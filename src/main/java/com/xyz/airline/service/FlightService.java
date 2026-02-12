package com.xyz.airline.service;

import com.xyz.airline.domain.Flight;
import com.xyz.airline.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public List<Flight> searchFlights(String origin, String destination, LocalDate departureDate) {
        Instant from = departureDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = departureDate.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        return flightRepository.findByOriginAndDestinationAndDepartureTimeBetween(origin, destination, from, to);
    }
}

