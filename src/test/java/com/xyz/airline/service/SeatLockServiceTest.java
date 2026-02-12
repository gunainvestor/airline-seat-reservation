package com.xyz.airline.service;

import com.xyz.airline.api.dto.SeatLockRequest;
import com.xyz.airline.api.dto.SeatLockResponse;
import com.xyz.airline.domain.SeatLock;
import com.xyz.airline.repository.SeatLockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SeatLockServiceTest {

    @Test
    void lockSeatsSucceedsWhenSeatsAvailable() {
        StringRedisTemplate redis = mock(StringRedisTemplate.class);
        SeatLockRepository repo = mock(SeatLockRepository.class);
        SeatLockService service = new SeatLockService(redis, repo);

        when(redis.opsForValue().setIfAbsent(anyString(), anyString(), anyLong(), any()))
                .thenReturn(true);

        SeatLockRequest req = new SeatLockRequest();
        req.setFlightId("FL1");
        req.setSeatNumbers(List.of("12A", "12B"));
        req.setUserId("U1");

        SeatLockResponse resp = service.lockSeats(req);

        assertThat(resp.getLockId()).isNotNull();
        verify(repo, times(1)).save(any(SeatLock.class));
    }

    @Test
    void lockSeatsFailsWhenSeatAlreadyLocked() {
        StringRedisTemplate redis = mock(StringRedisTemplate.class);
        SeatLockRepository repo = mock(SeatLockRepository.class);
        SeatLockService service = new SeatLockService(redis, repo);

        when(redis.opsForValue().setIfAbsent(anyString(), anyString(), anyLong(), any()))
                .thenReturn(true)
                .thenReturn(false);

        SeatLockRequest req = new SeatLockRequest();
        req.setFlightId("FL1");
        req.setSeatNumbers(List.of("12A", "12B"));
        req.setUserId("U1");

        assertThrows(IllegalStateException.class, () -> service.lockSeats(req));
    }
}

