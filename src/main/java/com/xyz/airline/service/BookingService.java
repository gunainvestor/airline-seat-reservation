package com.xyz.airline.service;

import com.xyz.airline.api.dto.BookingConfirmRequest;
import com.xyz.airline.domain.Booking;
import com.xyz.airline.domain.Seat;
import com.xyz.airline.domain.SeatLock;
import com.xyz.airline.repository.BookingRepository;
import com.xyz.airline.repository.SeatLockRepository;
import com.xyz.airline.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatLockRepository seatLockRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final PaymentGateway paymentGateway;

    @Transactional
    public Booking confirmBooking(BookingConfirmRequest request) {
        SeatLock lock = seatLockRepository.findById(request.getLockId())
                .orElseThrow(() -> new IllegalArgumentException("Lock not found"));

        if (lock.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("Lock expired");
        }

        BigDecimal totalAmount = BigDecimal.valueOf(100L * request.getPassengers().size());

        String paymentId = paymentGateway.charge(request.getPaymentToken(), totalAmount);

        Booking booking = new Booking();
        booking.setId(UUID.randomUUID().toString());
        booking.setPnr(generatePnr());
        booking.setFlightId(lock.getFlightId());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setTotalAmount(totalAmount);
        booking.setCurrency("EUR");
        booking.setPaymentId(paymentId);
        booking.setCreatedAt(Instant.now());
        booking.setUpdatedAt(Instant.now());
        booking.setPassengers(
                request.getPassengers().stream()
                        .map(p -> Booking.PassengerAssignment.builder()
                                .firstName(p.getFirstName())
                                .lastName(p.getLastName())
                                .email(p.getEmail())
                                .seatNumber(p.getSeatNumber())
                                .fareClassCode(p.getFareClassCode())
                                .build())
                        .collect(Collectors.toList())
        );
        bookingRepository.save(booking);

        List<Seat> seats = seatRepository.findAllById(
                lock.getSeatNumbers().stream()
                        .map(seat -> lock.getFlightId() + "-" + seat)
                        .toList()
        );
        seats.forEach(s -> {
            s.setStatus(Seat.SeatStatus.BOOKED);
            s.setBookingId(booking.getId());
        });
        seatRepository.saveAll(seats);

        return booking;
    }

    private String generatePnr() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}

