package com.xyz.airline.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingConfirmResponse {

    private String bookingId;
    private String pnr;
    private String status;
}

