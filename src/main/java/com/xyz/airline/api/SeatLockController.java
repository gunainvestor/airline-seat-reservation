package com.xyz.airline.api;

import com.xyz.airline.api.dto.SeatLockRequest;
import com.xyz.airline.api.dto.SeatLockResponse;
import com.xyz.airline.service.SeatLockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SeatLockController {

    private final SeatLockService seatLockService;

    @PostMapping("/api/bookings/lock")
    public SeatLockResponse lockSeats(@Valid @RequestBody SeatLockRequest request) {
        return seatLockService.lockSeats(request);
    }
}

