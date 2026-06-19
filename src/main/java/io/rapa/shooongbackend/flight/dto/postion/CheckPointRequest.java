package io.rapa.shooongbackend.flight.dto.postion;


public record CheckPointRequest(
        Long targetCheckpointIndex,
        Long totalCheckpointCount,
        String targetCheckpointName,
        String targetCheckpointStatus,
        Boolean isFinalCheckpoint,
        Double distanceToTarget,
        PositionRequest targetCheckpointPosition
) {
}
