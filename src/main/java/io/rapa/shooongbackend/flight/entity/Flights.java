package io.rapa.shooongbackend.flight.entity;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.exception.CustomException;
import io.rapa.shooongbackend.common.util.PreConditions;
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

    @Column(nullable = false)
    private Boolean isCrashed;

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
        this.isCrashed = false;
        this.startedAt = Instant.now();
        order.addFlight(this);
    }

    public void setCrashed(){
        this.isCrashed = true;
        finishedAt = Instant.now();
        flightStatus = FlightStatus.CRASHED;
        calculateTotalTime();
    }

    public void setCompleted(){
        finishedAt = Instant.now();
        flightStatus = FlightStatus.FLIGHT_COMPLETE;
        calculateTotalTime();
    }

    public void calculateTotalTime(){
        if(recordList.size() <= 0) order.setTotalFlightTime(0L);
        else order.setTotalFlightTime(
                recordList.get(recordList.size() - 1).getTimestampUnixMs()
                        - recordList.get(0).getTimestampUnixMs() / 1000
        );
    }

    public void addFlightRecord(FlightRecords flightRecord){
        this.recordList.add(flightRecord);
    }

    public void addAllFlightRecord(List<FlightRecords> records){
        this.recordList.addAll(records);
    }
}
