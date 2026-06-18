package io.rapa.shooongbackend.waypoint.repository;

import io.rapa.shooongbackend.waypoint.entity.WayPoints;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WayPointRepository extends JpaRepository<WayPoints, Long> {

}
