package io.rapa.shooongbackend.flight.entity;

import io.rapa.shooongbackend.common.entity.BaseEntity;
import io.rapa.shooongbackend.flight.constant.FlightStatus;
import io.rapa.shooongbackend.flightrecord.FlightRecords;
import io.rapa.shooongbackend.order.Orders;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Flights extends BaseEntity {
    @Id
    @Column(nullable = false, updatable = false)
    private Long flightId;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @OneToMany(mappedBy = "flight")
    private List<FlightRecords> recordList;

    @Builder
    public Flights(
            Orders order
    ){
        this.flightStatus = FlightStatus.IN_FLIGHT;
        this.order = order;
        order.addFlight(this);
    }

    public void addFlightRecord(FlightRecords flightRecord){
        this.recordList.add(flightRecord);
    }

    public void addAllFlightRecord(List<FlightRecords> records){
        this.recordList.addAll(records);
    }
}
