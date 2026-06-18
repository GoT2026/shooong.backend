package io.rapa.shooongbackend.flight.repository;


import io.rapa.shooongbackend.flight.entity.Flights;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flights, Long> {
}
