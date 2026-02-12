package com.xyz.airline.repository;

import com.xyz.airline.domain.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Booking, String> {
}

