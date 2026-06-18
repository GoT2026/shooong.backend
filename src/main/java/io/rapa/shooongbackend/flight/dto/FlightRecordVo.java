package io.rapa.shooongbackend.flight.dto;

import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import io.rapa.shooongbackend.flight.dto.postion.RotationRequest;

public record FlightRecordVo(
    Long sequence,
    Long timestampUnixMs,
    Double elapsedTimeSeconds,
    PositionRequest position,
    RotationRequest rotation
) {
}
