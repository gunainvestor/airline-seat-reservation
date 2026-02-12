package com.xyz.airline.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class SeatLockResponse {

    private String lockId;
    private Instant expiresAt;
}

