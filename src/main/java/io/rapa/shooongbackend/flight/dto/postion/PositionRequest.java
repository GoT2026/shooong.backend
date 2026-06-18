package io.rapa.shooongbackend.flight.dto.postion;

import io.rapa.shooongbackend.flightrecord.entity.Position;

public record PositionRequest(
        Double  x,
        Double  y,
        Double  z
) {
}
