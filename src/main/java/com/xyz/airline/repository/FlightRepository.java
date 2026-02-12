package com.xyz.airline.repository;

import com.xyz.airline.domain.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface FlightRepository extends MongoRepository<Flight, String> {

    List<Flight> findByOriginAndDestinationAndDepartureTimeBetween(
            String origin,
            String destination,
            Instant from,
            Instant to
    );
}

