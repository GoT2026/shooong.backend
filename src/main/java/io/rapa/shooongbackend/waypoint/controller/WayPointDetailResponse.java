package io.rapa.shooongbackend.waypoint.controller;

import io.rapa.shooongbackend.flightrecord.entity.Position;
import io.rapa.shooongbackend.waypoint.entity.WayPoints;

import java.time.Instant;

public record WayPointDetailResponse(
        Long wayPointId,
        Position position,
        Instant passedTime
) {
    public static WayPointDetailResponse from(WayPoints wayPoints){
        return new WayPointDetailResponse(
                wayPoints.getWayPointId(),
                wayPoints.getPosition(),
                wayPoints.getPassedTime()
        );
    }
}
