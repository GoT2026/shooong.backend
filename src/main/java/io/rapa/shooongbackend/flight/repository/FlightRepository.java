package io.rapa.shooongbackend.flight.repository;


import io.rapa.shooongbackend.flight.entity.Flights;
import io.rapa.shooongbackend.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flights, Long> {

    boolean existsByOrder(Orders order);
}
