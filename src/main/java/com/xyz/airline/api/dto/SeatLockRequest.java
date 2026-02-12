package com.xyz.airline.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SeatLockRequest {

    @NotBlank
    private String flightId;

    @NotEmpty
    private List<String> seatNumbers;

    @NotBlank
    private String userId;
}

