package com.xyz.airline.service;

import com.xyz.airline.api.dto.BookingConfirmRequest;
import com.xyz.airline.domain.Booking;
import com.xyz.airline.domain.Seat;
import com.xyz.airline.domain.SeatLock;
import com.xyz.airline.repository.BookingRepository;
import com.xyz.airline.repository.SeatLockRepository;
import com.xyz.airline.repository.SeatRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Test
    void confirmBookingSucceeds() {
        SeatLockRepository lockRepo = mock(SeatLockRepository.class);
        BookingRepository bookingRepo = mock(BookingRepository.class);
        SeatRepository seatRepo = mock(SeatRepository.class);
        PaymentGateway paymentGateway = mock(PaymentGateway.class);

        BookingService service = new BookingService(lockRepo, bookingRepo, seatRepo, paymentGateway);

        SeatLock lock = SeatLock.builder()
                .id("LOCK-1")
                .flightId("FL1")
                .seatNumbers(List.of("12A"))
                .expiresAt(Instant.now().plusSeconds(300))
                .build();
        when(lockRepo.findById("LOCK-1")).thenReturn(Optional.of(lock));
        when(paymentGateway.charge(anyString(), any(BigDecimal.class))).thenReturn("PAY-1");

        when(bookingRepo.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));
        when(seatRepo.findAllById(anyList())).thenReturn(
                List.of(Seat.builder().id("FL1-12A").flightId("FL1").seatNumber("12A").build())
        );

        BookingConfirmRequest req = new BookingConfirmRequest();
        req.setLockId("LOCK-1");
        BookingConfirmRequest.PassengerDto pax = new BookingConfirmRequest.PassengerDto();
        pax.setFirstName("John");
        pax.setLastName("Doe");
        pax.setEmail("john@example.com");
        pax.setSeatNumber("12A");
        pax.setFareClassCode("Y");
        req.setPassengers(List.of(pax));
        req.setPaymentToken("tok_123");

        Booking booking = service.confirmBooking(req);

        assertThat(booking.getStatus()).isEqualTo(Booking.BookingStatus.CONFIRMED);
        assertThat(booking.getPassengers()).hasSize(1);
    }

    @Test
    void confirmBookingFailsWhenLockExpired() {
        SeatLockRepository lockRepo = mock(SeatLockRepository.class);
        BookingRepository bookingRepo = mock(BookingRepository.class);
        SeatRepository seatRepo = mock(SeatRepository.class);
        PaymentGateway paymentGateway = mock(PaymentGateway.class);

        BookingService service = new BookingService(lockRepo, bookingRepo, seatRepo, paymentGateway);

        SeatLock lock = SeatLock.builder()
                .id("LOCK-1")
                .flightId("FL1")
                .seatNumbers(List.of("12A"))
                .expiresAt(Instant.now().minusSeconds(10))
                .build();
        when(lockRepo.findById("LOCK-1")).thenReturn(Optional.of(lock));

        BookingConfirmRequest req = new BookingConfirmRequest();
        req.setLockId("LOCK-1");
        BookingConfirmRequest.PassengerDto pax = new BookingConfirmRequest.PassengerDto();
        pax.setFirstName("John");
        pax.setLastName("Doe");
        pax.setEmail("john@example.com");
        pax.setSeatNumber("12A");
        pax.setFareClassCode("Y");
        req.setPassengers(List.of(pax));
        req.setPaymentToken("tok_123");

        assertThrows(IllegalStateException.class, () -> service.confirmBooking(req));
    }
}

