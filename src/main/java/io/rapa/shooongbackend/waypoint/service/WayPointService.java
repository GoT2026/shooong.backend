package io.rapa.shooongbackend.waypoint.service;

import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.order.repository.OrderRepository;
import io.rapa.shooongbackend.waypoint.controller.WayPointDetailResponse;
import io.rapa.shooongbackend.waypoint.dto.WayPointCreateRequest;
import io.rapa.shooongbackend.waypoint.entity.WayPoints;
import io.rapa.shooongbackend.waypoint.repository.WayPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class WayPointService {
    private final WayPointRepository wayPointRepository;
    private final OrderRepository orderRepository;


    @PreAuthorize("isAuthenticated()")
    public void createWayPoint(WayPointCreateRequest request){
        Orders founded = orderRepository.findByIdOrThrow(request.orderId());
        wayPointRepository.save(
                WayPoints.builder()
                        .order(founded)
                        .position(request.position())
                        .build()
        );
    }

    @PreAuthorize("isAuthenticated()")
    public List<WayPointDetailResponse> getWayPoint(Long orderId){
        Orders foundedOrder = orderRepository.findByIdOrThrow(orderId);
        return wayPointRepository.findAllByOrder(foundedOrder).stream().map((wayPoint) -> WayPointDetailResponse.from(wayPoint))
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    public void passedWayPoint(Long wayPointId){
        WayPoints founded = wayPointRepository.findByIdOrThrow(wayPointId);
        founded.passed();
        founded.getOrder().deductWayPointCnt();
        log.info(founded.getPassedTime().toString());
    }
}
