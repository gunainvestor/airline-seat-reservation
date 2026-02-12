package com.xyz.airline.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BookingConfirmRequest {

    @NotBlank
    private String lockId;

    @NotEmpty
    private List<PassengerDto> passengers;

    @NotBlank
    private String paymentToken;

    @Data
    public static class PassengerDto {
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        @NotBlank
        private String email;
        @NotBlank
        private String seatNumber;
        @NotBlank
        private String fareClassCode;
    }
}

