package io.rapa.shooongbackend.flight.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.flight.dto.FlightRecordRequest;
import io.rapa.shooongbackend.flight.dto.FlightReplayResponse;
import io.rapa.shooongbackend.flight.dto.StartFlightResponse;
import io.rapa.shooongbackend.flight.service.FlightService;
import io.rapa.shooongbackend.ranking.dto.LeaderboardResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/record")
    public ResponseEntity<ApiResult<Void>> recordFlight(
            @RequestBody FlightRecordRequest request
    ){
        flightService.recordFlights(request);
        return ApiResult.empty(
            SuccessCode.FLIGHT_RECORD
        );
    }

    @GetMapping("/{flightId}/replay")
    public ResponseEntity<ApiResult<FlightReplayResponse>> getReplay(
            @PathVariable Long flightId
    ) {
        return ApiResult.data(
                SuccessCode.FLIGHT_REPLAY_RETRIEVE,
                flightService.getReplay(flightId)
        );
    }



    @PutMapping("/{flightId}/crash")
    public ResponseEntity<ApiResult<Void>> setCrashed(
            @PathVariable Long flightId
    ){
            flightService.setCrashed(flightId);
            return ApiResult.empty(
                SuccessCode.FLIGHT_FINISHED
            );
    }

    @PutMapping("/{flightId}/complete")
    public ResponseEntity<ApiResult<LeaderboardResultResponse>> flightComplete(
            @PathVariable Long flightId
    ){
        return ApiResult.data(
                SuccessCode.FLIGHT_FINISHED,
                flightService.setCompleted(flightId)
        );
    }
}
