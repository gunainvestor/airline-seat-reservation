package com.xyz.airline.repository;

import com.xyz.airline.domain.SeatLock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeatLockRepository extends MongoRepository<SeatLock, String> {
}

