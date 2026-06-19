package io.rapa.shooongbackend.flight.dto;

import io.rapa.shooongbackend.flight.dto.checkpoint.CheckPointRequest;
import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import io.rapa.shooongbackend.flight.dto.postion.RotationRequest;

public record FlightRecordVo(
    Long sequence,
    Long timestampUnixMs,
    Double elapsedTimeSeconds,
    PositionRequest position,
    RotationRequest rotation,
    CheckPointRequest checkpoint
) {
    public FlightRecordVo(
            Long sequence,
            Long timestampUnixMs,
            Double elapsedTimeSeconds,
            PositionRequest position,
            RotationRequest rotation
    ) {
        this(
                sequence,
                timestampUnixMs,
                elapsedTimeSeconds,
                position,
                rotation,
                null
        );
    }
}
