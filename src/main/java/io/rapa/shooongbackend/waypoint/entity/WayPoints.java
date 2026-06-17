package io.rapa.shooongbackend.waypoint.entity;

import io.rapa.shooongbackend.order.Orders;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WayPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wayPointId;

    @Column(nullable = false, length = 255)
    private String position;

    private Instant passedTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @Builder
    public WayPoints(
            String position,
            Orders order
    ){
        this.position = position;
        this.order = order;
        order.addWayPoint(this);
    }
}
