package com.xyz.airline.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("seats")
public class Seat {

    @Id
    private String id; // flightId-seatNumber

    private String flightId;
    private String seatNumber;
    private String cabin;
    private String fareClassCode;
    private SeatStatus status;
    private String bookingId;

    public enum SeatStatus {
        AVAILABLE, BOOKED
    }
}

