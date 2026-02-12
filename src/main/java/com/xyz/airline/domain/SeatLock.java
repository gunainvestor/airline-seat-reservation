package com.xyz.airline.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("seatLocks")
public class SeatLock {

    @Id
    private String id;
    private String flightId;
    private List<String> seatNumbers;
    private String userId;
    private Instant expiresAt;
    private SeatLockStatus status;

    public enum SeatLockStatus {
        ACTIVE, EXPIRED, RELEASED, CONVERTED_TO_BOOKING
    }
}

