package io.rapa.shooongbackend.flight.repository;


import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.exception.CustomException;
import io.rapa.shooongbackend.flight.entity.Flights;
import io.rapa.shooongbackend.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flights, Long> {

    default Flights findByIdOrThrow(Long flightId){
        return findById(flightId).orElseThrow(
                ()-> new CustomException(ErrorCode.FLIGHT_NOT_FOUND)
        );
    }

    boolean existsByOrder(Orders order);
}
