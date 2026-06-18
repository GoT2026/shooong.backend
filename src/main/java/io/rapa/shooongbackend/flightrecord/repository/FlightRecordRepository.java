package io.rapa.shooongbackend.flightrecord.repository;

import io.rapa.shooongbackend.flight.entity.Flights;
import io.rapa.shooongbackend.flightrecord.entity.FlightRecords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FlightRecordRepository extends JpaRepository<FlightRecords, UUID> {
    List<FlightRecords> findAllByFlightOrderBySequenceAsc(Flights flight);
}
