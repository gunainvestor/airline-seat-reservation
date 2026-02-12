package com.xyz.airline.repository;

import com.xyz.airline.domain.Seat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SeatRepository extends MongoRepository<Seat, String> {

    List<Seat> findByFlightId(String flightId);
}

