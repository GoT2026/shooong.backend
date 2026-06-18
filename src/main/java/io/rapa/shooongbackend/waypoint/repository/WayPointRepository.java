package io.rapa.shooongbackend.waypoint.repository;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.exception.CustomException;
import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.waypoint.entity.WayPoints;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WayPointRepository extends JpaRepository<WayPoints, Long> {

    List<WayPoints> findAllByOrder(Orders order);

    default WayPoints findByIdOrThrow(Long wayPointId){
        return findById(wayPointId).orElseThrow(
                ()-> new CustomException(ErrorCode.WAY_POINT_NOT_FOUND)
        );
    }
}
