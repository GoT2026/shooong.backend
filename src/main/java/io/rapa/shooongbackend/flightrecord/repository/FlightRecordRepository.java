package io.rapa.shooongbackend.flightrecord.repository;

import io.rapa.shooongbackend.flightrecord.entity.FlightRecords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FlightRecordRepository extends JpaRepository<FlightRecords, UUID> {

}
