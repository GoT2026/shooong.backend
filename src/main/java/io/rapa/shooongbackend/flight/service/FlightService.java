package io.rapa.shooongbackend.flight.service;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.util.PreConditions;
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

        PreConditions.validate(
            !flightRepository.existsByOrder(founded),
            ErrorCode.ALREADY_ASSIGNED_ORDER
        );

        return StartFlightResponse.from(
                flightRepository.save(
                        new Flights(founded)
                )
        );
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void recordFlights(
            FlightRecordRequest request
    ){
        Flights founded = flightRepository.findByIdOrThrow(request.flightId());

        List<FlightRecords> list = request.samples().stream().map(
                (record) -> FlightRecords.builder()
                        .timeStamp(Instant.ofEpochMilli(record.timestampUnixMs()))
                        .positionRequest(record.position())
                        .rotationRequest(record.rotation())
                        .flight(founded)
                        .build()
        ).toList();
        founded.addAllFlightRecord(list);
        flightRecordRepository.saveAll(list);
    }
}
