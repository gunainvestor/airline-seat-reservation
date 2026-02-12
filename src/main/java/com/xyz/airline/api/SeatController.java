package com.xyz.airline.api;

import com.xyz.airline.domain.Seat;
import com.xyz.airline.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/api/flights/{flightId}/seats")
    public List<Seat> getSeats(@PathVariable String flightId) {
        return seatService.getSeatsForFlight(flightId);
    }
}

