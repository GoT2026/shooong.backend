package io.rapa.shooongbackend.flight.entity;

import io.rapa.shooongbackend.flight.constant.FlightStatus;
import io.rapa.shooongbackend.flightrecord.entity.FlightRecords;
import io.rapa.shooongbackend.order.entity.Orders;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Flights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @OneToMany(mappedBy = "flight")
    private List<FlightRecords> recordList = new ArrayList<>();

    @Column(nullable = false)
    private Instant startedAt;

    private Instant finishedAt;

    @Builder
    public Flights(
            Orders order
    ){
        this.flightStatus = FlightStatus.IN_FLIGHT;
        this.order = order;
        this.startedAt = Instant.now();
        order.addFlight(this);
    }

    public void addFlightRecord(FlightRecords flightRecord){
        this.recordList.add(flightRecord);
    }

    public void addAllFlightRecord(List<FlightRecords> records){
        this.recordList.addAll(records);
    }
}
