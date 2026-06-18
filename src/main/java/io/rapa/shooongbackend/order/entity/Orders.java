package io.rapa.shooongbackend.order.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rapa.shooongbackend.common.entity.BaseEntity;
import io.rapa.shooongbackend.flight.entity.Flights;
import io.rapa.shooongbackend.member.entity.Members;
import io.rapa.shooongbackend.order.constant.OrderStatus;
import io.rapa.shooongbackend.waypoint.entity.WayPoints;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders extends BaseEntity {
    @Id
    @Column(nullable = false , updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = true)
    private Long rating;

    @Column(nullable = true)
    private Double score;

    @Column(nullable = true)
    private Long totalFlightTime;

    @Column(nullable = true)
    private Double averageTilt;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Integer remainWaypointCnt;

    @ManyToOne
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    private Members member;

    @OneToMany(mappedBy = "order")
    private List<WayPoints> wayPoints = new ArrayList<>();

    @OneToMany(mappedBy = "order")
    private List<Flights> flights = new ArrayList<>();

    @Builder
    public Orders(
            Members member
    ){
        this.member = member;
        this.orderStatus = OrderStatus.PROCESSING;
        this.remainWaypointCnt = 0;
        member.addOrder(this);
    }

    public void setTotalFlightTime(Long totalFlightTime) {
        this.totalFlightTime = totalFlightTime;
    }

    public void deductWayPointCnt(){
        this.remainWaypointCnt--;
    }

    public void addFlight(Flights flight){
        this.flights.add(flight);
    }

    public void addWayPoint(WayPoints wayPoint){
        this.wayPoints.add(wayPoint);
        remainWaypointCnt++;
    }
}
