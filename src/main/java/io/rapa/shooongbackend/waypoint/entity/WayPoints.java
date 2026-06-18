package io.rapa.shooongbackend.waypoint.entity;

import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import io.rapa.shooongbackend.flightrecord.entity.Position;
import io.rapa.shooongbackend.order.entity.Orders;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WayPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wayPointId;

    @Embedded
    private Position position;

    private Instant passedTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @Builder
    public WayPoints(
            PositionRequest position,
            Orders order
    ){
        this.position = Position.from(position);
        this.order = order;
        order.addWayPoint(this);
    }

    public void passed(){
        this.passedTime = Instant.now();
    }
}
