package io.rapa.shooongbackend.flightrecord.entity;

import io.rapa.shooongbackend.flight.dto.checkpoint.CheckPointRequest;
import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlightRecordCheckpoint {

    @Column(name = "target_checkpoint_index")
    private Integer targetCheckpointIndex;

    @Column(name = "total_checkpoint_count")
    private Integer totalCheckpointCount;

    @Column(name = "target_checkpoint_name")
    private String targetCheckpointName;

    @Column(name = "target_checkpoint_status")
    private String targetCheckpointStatus;

    @Column(name = "is_final_checkpoint")
    private Boolean isFinalCheckpoint;

    @Column(name = "distance_to_target")
    private Double distanceToTarget;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "posX", column = @Column(name = "chk_posx", nullable = false)),
            @AttributeOverride(name = "posY", column = @Column(name = "chk_posy", nullable = false)),
            @AttributeOverride(name = "posZ", column = @Column(name = "chk_posz", nullable = false))
    })
    private Position targetCheckpointPosition;

    private FlightRecordCheckpoint(
            Integer targetCheckpointIndex,
            Integer totalCheckpointCount,
            String targetCheckpointName,
            String targetCheckpointStatus,
            Boolean isFinalCheckpoint,
            Double distanceToTarget,
            Position targetCheckpointPosition
    ) {
        this.targetCheckpointIndex = targetCheckpointIndex;
        this.totalCheckpointCount = totalCheckpointCount;
        this.targetCheckpointName = targetCheckpointName;
        this.targetCheckpointStatus = targetCheckpointStatus;
        this.isFinalCheckpoint = isFinalCheckpoint;
        this.distanceToTarget = distanceToTarget;
        this.targetCheckpointPosition = targetCheckpointPosition;
    }

    public static FlightRecordCheckpoint from(CheckPointRequest request) {
        if (request == null) {
            return empty();
        }

        PositionRequest positionRequest = request.targetCheckpointPosition();
        Position checkpointPosition = positionRequest == null
                ? new Position(0.0, 0.0, 0.0)
                : Position.from(positionRequest);

        return new FlightRecordCheckpoint(
                defaultInt(request.targetCheckpointIndex()),
                defaultInt(request.totalCheckpointCount()),
                defaultString(request.targetCheckpointName()),
                defaultString(request.targetCheckpointStatus()),
                request.isFinalCheckpoint() != null && request.isFinalCheckpoint(),
                defaultDouble(request.distanceToTarget()),
                checkpointPosition
        );
    }

    public CheckPointRequest toRequest() {
        Position position = targetCheckpointPosition == null
                ? new Position(0.0, 0.0, 0.0)
                : targetCheckpointPosition;

        return new CheckPointRequest(
                targetCheckpointIndex,
                totalCheckpointCount,
                targetCheckpointName,
                targetCheckpointStatus,
                isFinalCheckpoint,
                distanceToTarget,
                new PositionRequest(
                        position.getPosX(),
                        position.getPosY(),
                        position.getPosZ()
                )
        );
    }

    private static FlightRecordCheckpoint empty() {
        return new FlightRecordCheckpoint(
                0,
                0,
                "",
                "",
                false,
                0.0,
                new Position(0.0, 0.0, 0.0)
        );
    }

    private static Integer defaultInt(Integer value) {
        return value == null ? 0 : value;
    }

    private static Double defaultDouble(Double value) {
        return value == null ? 0.0 : value;
    }

    private static String defaultString(String value) {
        return value == null ? "" : value;
    }
}
