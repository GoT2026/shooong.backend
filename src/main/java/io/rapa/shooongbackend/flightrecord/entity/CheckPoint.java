package io.rapa.shooongbackend.flightrecord.entity;

import io.rapa.shooongbackend.flight.dto.postion.CheckPointRequest;
import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckPoint {
    private Long targetCheckpointIndex;
    private Long totalCheckpointCount;
    private String targetCheckpointName;
    private String targetCheckpointStatus;
    private Boolean isFinalCheckpoint;
    private Double distanceToTarget;
    private CheckPointPosition targetCheckpointPosition;

    public CheckPoint(
            Long targetCheckpointIndex,
            Long totalCheckpointCount,
            String targetCheckpointName, String targetCheckpointStatus, Boolean isFinalCheckpoint, Double distanceToTarget, PositionRequest targetCheckpointPosition) {
        this.targetCheckpointIndex = targetCheckpointIndex;
        this.totalCheckpointCount = totalCheckpointCount;
        this.targetCheckpointName = targetCheckpointName;
        this.targetCheckpointStatus = targetCheckpointStatus;
        this.isFinalCheckpoint = isFinalCheckpoint;
        this.distanceToTarget = distanceToTarget;
        this.targetCheckpointPosition = CheckPointPosition.from(targetCheckpointPosition);
    }

    public static CheckPoint from(CheckPointRequest request){
        return new CheckPoint(
                request.targetCheckpointIndex(),
                request.totalCheckpointCount(),
                request.targetCheckpointName(),
                request.targetCheckpointStatus(),
                request.isFinalCheckpoint(),
                request.distanceToTarget(),
                request.targetCheckpointPosition()
        );
    }

}
