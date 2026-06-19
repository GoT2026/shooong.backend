package io.rapa.shooongbackend.flightrecord.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rapa.shooongbackend.flight.dto.checkpoint.CheckPointRequest;
import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import io.rapa.shooongbackend.flight.dto.postion.RotationRequest;
import io.rapa.shooongbackend.flight.entity.Flights;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlightRecords {

    @Id
    @UuidGenerator
    @Column(nullable = false , updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID recordId;

    @Column(nullable = false)
    private Instant timeStamp;

    @Embedded
    private Position positions;

    @Embedded
    private Rotation rotations;

    @Embedded
    private Coordinate coordinate;

    @Embedded
    private FlightRecordCheckpoint checkpoint;

    @Column(nullable = false)
    private Long sequence;

    @Column(nullable = false)
    private Long timestampUnixMs;

    @Column(nullable = false)
    private Double elapsedTimeSeconds;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flights flight;

    @Builder
    public FlightRecords(
            Instant timeStamp,
            PositionRequest positionRequest,
            RotationRequest rotationRequest,
            CheckPointRequest checkpointRequest,
            Long sequence,
            Long timestampUnixMs,
            Double elapsedTimeSeconds,
            Flights flight
    ) {
        this.timeStamp = timeStamp;
        this.positions = Position.from(positionRequest);
        this.rotations = Rotation.from(rotationRequest);
        this.sequence = sequence;
        this.timestampUnixMs = timestampUnixMs;
        this.elapsedTimeSeconds = elapsedTimeSeconds;
        this.flight = flight;
        this.coordinate = Coordinate.from(rotations);
        this.checkpoint = FlightRecordCheckpoint.from(checkpointRequest);
    }
}
