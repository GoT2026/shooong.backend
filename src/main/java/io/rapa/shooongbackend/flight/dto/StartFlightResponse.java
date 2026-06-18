package io.rapa.shooongbackend.flight.dto;

import io.rapa.shooongbackend.flight.entity.Flights;

import java.time.Instant;

public record StartFlightResponse(
        Long flightId,
        Long OrderId,
        Instant createdAt,
        Instant finishedAt,
        String flightStatus
) {
    public static StartFlightResponse from(Flights flights){
        return new StartFlightResponse(
                flights.getFlightId(),
                flights.getOrder().getOrderId(),
                flights.getStartedAt(),
                flights.getFinishedAt(),
                flights.getFlightStatus().name()
        );
    }
}
