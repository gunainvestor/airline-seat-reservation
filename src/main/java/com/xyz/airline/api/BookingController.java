package com.xyz.airline.api;

import com.xyz.airline.api.dto.BookingConfirmRequest;
import com.xyz.airline.api.dto.BookingConfirmResponse;
import com.xyz.airline.domain.Booking;
import com.xyz.airline.repository.BookingRepository;
import com.xyz.airline.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @PostMapping("/api/bookings/confirm")
    public BookingConfirmResponse confirm(@Valid @RequestBody BookingConfirmRequest request) {
        Booking booking = bookingService.confirmBooking(request);
        return new BookingConfirmResponse(booking.getId(), booking.getPnr(), booking.getStatus().name());
    }

    @GetMapping("/api/bookings/{bookingId}")
    public Booking get(@PathVariable String bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }
}

