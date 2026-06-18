package io.rapa.shooongbackend.flight.service;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.util.PreConditions;
import io.rapa.shooongbackend.flight.dto.StartFlightResponse;
import io.rapa.shooongbackend.flight.entity.Flights;
import io.rapa.shooongbackend.flight.repository.FlightRepository;
import io.rapa.shooongbackend.member.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class FlightService {

    private final FlightRepository flightRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public StartFlightResponse startFlight(Long memberId, Long orderId){
        Orders founded = orderRepository.findByIdOrThrow(orderId);

        PreConditions.validate(
                founded.getMember().getMemberId().equals(memberId),
                ErrorCode.ORDER_NOT_VALID
        );

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

}
