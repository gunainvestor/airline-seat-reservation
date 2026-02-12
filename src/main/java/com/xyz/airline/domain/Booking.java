package com.xyz.airline.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("bookings")
public class Booking {

    @Id
    private String id;
    private String pnr;
    private String flightId;
    private BookingStatus status;
    private List<PassengerAssignment> passengers;
    private BigDecimal totalAmount;
    private String currency;
    private String paymentId;
    private Instant createdAt;
    private Instant updatedAt;

    public enum BookingStatus {
        PENDING_PAYMENT, CONFIRMED, CANCELLED, PAYMENT_FAILED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerAssignment {
        private String firstName;
        private String lastName;
        private String email;
        private String seatNumber;
        private String fareClassCode;
    }
}

