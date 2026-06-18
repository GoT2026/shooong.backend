package io.rapa.shooongbackend.waypoint.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.waypoint.dto.WayPointCreateRequest;
import io.rapa.shooongbackend.waypoint.entity.WayPoints;
import io.rapa.shooongbackend.waypoint.service.WayPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/waypoints")
public class WayPointController implements WayPointSwaggerSupporter{

    private final WayPointService wayPointService;
    
    @PostMapping
    public ResponseEntity<ApiResult<Void>> create(
            @RequestBody WayPointCreateRequest request
    ){
        wayPointService.createWayPoint(request);
        return ApiResult.empty(
                SuccessCode.WAY_POINT_CREATE_SUCCESS
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResult<List<WayPointDetailResponse>>> getWayPoints(
        @PathVariable Long orderId
    ){
        return ApiResult.data(
                SuccessCode.WAY_POINT_RETRIEVE_SUCCESS,
                wayPointService.getWayPoint(orderId)
        );
    }

    @PutMapping("/{wayPointId}")
    public ResponseEntity<ApiResult<Void>> passed(
            @PathVariable Long wayPointId
    ){
        wayPointService.passedWayPoint(wayPointId);
        return ApiResult.empty(
                SuccessCode.WAY_POINT_PASSED_SUCCESS
        );
    }
    
}
