package com.xyz.airline.service;

import com.xyz.airline.domain.Seat;
import com.xyz.airline.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    public List<Seat> getSeatsForFlight(String flightId) {
        return seatRepository.findByFlightId(flightId);
    }
}

