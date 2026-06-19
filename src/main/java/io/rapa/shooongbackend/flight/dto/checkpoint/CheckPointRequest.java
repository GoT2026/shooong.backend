package io.rapa.shooongbackend.flight.dto.checkpoint;

import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;

public record CheckPointRequest(
        Integer targetCheckpointIndex,
        Integer totalCheckpointCount,
        String targetCheckpointName,
        String targetCheckpointStatus,
        Boolean isFinalCheckpoint,
        Double distanceToTarget,
        PositionRequest targetCheckpointPosition
) {
}
