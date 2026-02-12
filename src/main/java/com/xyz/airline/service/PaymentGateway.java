package com.xyz.airline.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentGateway {

    public String charge(String paymentToken, BigDecimal amount) {
        if (paymentToken == null || paymentToken.isBlank()) {
            throw new IllegalArgumentException("Invalid payment token");
        }
        // Stubbed payment provider call
        return "PAY-" + java.util.UUID.randomUUID();
    }
}

