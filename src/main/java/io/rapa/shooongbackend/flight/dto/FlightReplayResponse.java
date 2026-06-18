package io.rapa.shooongbackend.flight.dto;

import io.rapa.shooongbackend.flight.entity.Flights;

import java.time.Instant;
import java.util.List;

public record FlightReplayResponse(
        Long flightId,
        Long orderId,
        Instant startedAt,
        Instant finishedAt,
        String flightStatus,
        List<FlightRecordVo> samples
) {
    public static FlightReplayResponse of(
            Flights flight,
            List<FlightRecordVo> samples
    ) {
        return new FlightReplayResponse(
                flight.getFlightId(),
                flight.getOrder().getOrderId(),
                flight.getStartedAt(),
                flight.getFinishedAt(),
                flight.getFlightStatus().name(),
                samples
        );
    }
}
