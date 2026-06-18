package io.rapa.shooongbackend.flight.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.flight.dto.FlightRecordRequest;
import io.rapa.shooongbackend.flight.dto.StartFlightResponse;
import io.rapa.shooongbackend.flight.service.FlightService;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController implements FlightSwaggerSupporter {
    private final FlightService flightService;

    @PostMapping("/{orderId}")
    public ResponseEntity<ApiResult<StartFlightResponse>> startFlight(
            @PathVariable Long orderId
    ){
        StartFlightResponse founded = flightService.startFlight(orderId);
        return ApiResult.data(
                SuccessCode.FLIGHT_START,
                founded
        );
    }

    @PostMapping("/{flightId}/record")
    public ResponseEntity<ApiResult<Void>> recordFlight(
            @PathVariable Long flightId,
            @RequestBody FlightRecordRequest request
    ){
        flightService.recordFlights(request);
        return ApiResult.empty(
            SuccessCode.FLIGHT_RECORD
        );
    }
}
