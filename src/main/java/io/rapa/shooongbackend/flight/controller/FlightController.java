package io.rapa.shooongbackend.flight.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.flight.dto.StartFlightResponse;
import io.rapa.shooongbackend.flight.service.FlightService;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController implements FlightSwaggerSupporter {
    private final FlightService flightService;

    @PostMapping("/{orderId}")
    public ResponseEntity<ApiResult<StartFlightResponse>> startFlight(
            @AuthenticationPrincipal DefaultCurrentUser currentUser,
            @PathVariable Long orderId
    ){
        StartFlightResponse founded = flightService.startFlight(currentUser.getUserId(), orderId);
        return ApiResult.data(
                SuccessCode.FLIGHT_START,
                founded
        );
    }
}
