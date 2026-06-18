package io.rapa.shooongbackend.flight.dto.postion;

public record RotationRequest(
        Double  x,
        Double  y,
        Double  z,
        Double  w
) {
}
