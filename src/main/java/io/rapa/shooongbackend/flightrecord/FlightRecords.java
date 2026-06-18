package io.rapa.shooongbackend.flightrecord;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rapa.shooongbackend.flight.entity.Flights;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

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

    @Column(nullable = false, length = 255)
    private String position;

    @Column(nullable = false, length = 255)
    private String rotation;

    @Column(nullable = false)
    private Double velocity;

    @Column(nullable = false)
    private Double pitch;

    @Column(nullable = false)
    private Double roll;

    @Column(nullable = false)
    private Double yaw;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flights flight;

    @Builder
    public FlightRecords(
            Instant timeStamp,
            String position,
            String rotation,
            Double velocity,
            Double pitch,
            Double roll,
            Double yaw,
            Flights flight
    ) {
        this.timeStamp = timeStamp;
        this.position = position;
        this.rotation = rotation;
        this.velocity = velocity;
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
        this.flight = flight;
        flight.addFlightRecord(this);
    }
}
