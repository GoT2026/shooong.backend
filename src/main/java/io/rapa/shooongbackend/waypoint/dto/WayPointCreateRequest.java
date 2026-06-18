package io.rapa.shooongbackend.waypoint.dto;

import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;

import java.util.List;

public record WayPointCreateRequest(
    Long orderId,
    PositionRequest position
) {
}
