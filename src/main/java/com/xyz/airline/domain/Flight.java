package com.xyz.airline.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("flights")
public class Flight {

    @Id
    private String id; // e.g. FL123-2026-02-20

    private String flightNumber;
    private String origin;
    private String destination;
    private Instant departureTime;
    private Instant arrivalTime;
}

