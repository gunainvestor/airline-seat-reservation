package com.xyz.airline.service;

import com.xyz.airline.api.dto.SeatLockRequest;
import com.xyz.airline.api.dto.SeatLockResponse;
import com.xyz.airline.domain.SeatLock;
import com.xyz.airline.repository.SeatLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SeatLockService {

    private static final Duration LOCK_TTL = Duration.ofMinutes(5);

    private final StringRedisTemplate redisTemplate;
    private final SeatLockRepository seatLockRepository;

    public SeatLockResponse lockSeats(SeatLockRequest request) {
        String lockId = "LOCK-" + java.util.UUID.randomUUID();
        Instant expiresAt = Instant.now().plus(LOCK_TTL);

        for (String seat : request.getSeatNumbers()) {
            String key = buildKey(request.getFlightId(), seat);
            Boolean success = redisTemplate.opsForValue()
                    .setIfAbsent(key, lockId, LOCK_TTL.toSeconds(), TimeUnit.SECONDS);

            if (Boolean.FALSE.equals(success)) {
                rollbackLocks(request.getFlightId(), request.getSeatNumbers(), lockId);
                throw new IllegalStateException("Seat already locked: " + seat);
            }
        }

        SeatLock seatLock = SeatLock.builder()
                .id(lockId)
                .flightId(request.getFlightId())
                .seatNumbers(request.getSeatNumbers())
                .userId(request.getUserId())
                .expiresAt(expiresAt)
                .status(SeatLock.SeatLockStatus.ACTIVE)
                .build();

        seatLockRepository.save(seatLock);

        return new SeatLockResponse(lockId, expiresAt);
    }

    private void rollbackLocks(String flightId, java.util.List<String> seats, String lockId) {
        seats.forEach(seat -> {
            String key = buildKey(flightId, seat);
            String current = redisTemplate.opsForValue().get(key);
            if (lockId.equals(current)) {
                redisTemplate.delete(key);
            }
        });
    }

    private String buildKey(String flightId, String seat) {
        return "seatlock:" + flightId + ":" + seat;
    }
}

