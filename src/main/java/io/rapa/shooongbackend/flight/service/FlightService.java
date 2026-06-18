package io.rapa.shooongbackend.flight.service;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.util.PreConditions;
import io.rapa.shooongbackend.flight.constant.FlightStatus;
import io.rapa.shooongbackend.flight.dto.FlightRecordRequest;
import io.rapa.shooongbackend.flight.dto.StartFlightResponse;
import io.rapa.shooongbackend.flight.entity.Flights;
import io.rapa.shooongbackend.flight.repository.FlightRepository;
import io.rapa.shooongbackend.flightrecord.entity.FlightRecords;
import io.rapa.shooongbackend.flightrecord.repository.FlightRecordRepository;
import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static io.rapa.shooongbackend.flight.constant.FlightStatus.CRASHED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class FlightService {

    private final FlightRepository flightRepository;
    private final OrderRepository orderRepository;
    private final FlightRecordRepository flightRecordRepository;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public StartFlightResponse startFlight(Long orderId){
        Orders founded = orderRepository.findByIdOrThrow(orderId);



        founded.getFlights().stream().forEach(
                (flight) -> PreConditions.validate(
                        !flight.getFlightStatus().equals(FlightStatus.IN_FLIGHT),
                        ErrorCode.ALREADY_ASSIGNED_ORDER
                )
        );

        return StartFlightResponse.from(
                flightRepository.save(
                        new Flights(founded)
                )
        );
    }


    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void setCrashed(Long flightId){
        Flights founded = flightRepository.findByIdOrThrow(flightId);
        founded.setCrashed();
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void setCompleted(Long flightId){
        Flights founded = flightRepository.findByIdOrThrow(flightId);
        founded.setCompleted();
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void recordFlights(
            FlightRecordRequest request
    ){
        Flights founded = flightRepository.findByIdOrThrow(request.flightId());

        PreConditions.validate(
                founded.getFlightStatus().equals(FlightStatus.IN_FLIGHT),
                ErrorCode.CAN_NOT_RECORD
        );

        List<FlightRecords> list = request.samples().stream().map(
                (record) -> FlightRecords.builder()
                        .timeStamp(Instant.ofEpochMilli(record.timestampUnixMs()))
                        .positionRequest(record.position())
                        .rotationRequest(record.rotation())
                        .sequence(record.sequence())
                        .timestampUnixMs(record.timestampUnixMs())
                        .elapsedTimeSeconds(record.elapsedTimeSeconds())
                        .flight(founded)
                        .build()
        ).toList();
        founded.addAllFlightRecord(list);
        flightRecordRepository.saveAll(list);
    }
}
