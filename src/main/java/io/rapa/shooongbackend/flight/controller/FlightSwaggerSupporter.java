package io.rapa.shooongbackend.flight.controller;

import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.flight.dto.FlightRecordRequest;
import io.rapa.shooongbackend.flight.dto.FlightReplayResponse;
import io.rapa.shooongbackend.flight.dto.StartFlightResponse;
import io.rapa.shooongbackend.ranking.dto.LeaderboardResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(
        name = "Flight",
        description = "비행 관련 API"
)
public interface FlightSwaggerSupporter {


    @Operation(
            summary = "비행 시작",
            description = "주문을 수행하기 위해 비행을 시작하는 API",
            parameters = {
                    @Parameter(name = "orderId", description = "주문 ID")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "201",
            description = "비행 시작 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode":"201 CREATED",
                                "message":"비행이 시작되었습니다.",
                                "data":{
                                    "flightId":1,
                                    "OrderId":1,
                                    "createdAt":"2026-06-18T05:41:44.688244100Z",
                                    "finishedAt":null,
                                    "flightStatus":"IN_FLIGHT"
                                }
                            }
                            """
                    )
            )
    )
    ResponseEntity<ApiResult<StartFlightResponse>> startFlight(Long orderId);


    @Operation(
            summary = "비행 기록",
            description = "비행을 기록하는 API",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @RequestBody(
            content = @Content(
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "flightId": 1,
                                        "samples": [
                                            {
                                                "sequence": 1,
                                                "timestampUnixMs": 178175000000,
                                                "elapsedTimeSeconds": 0.011,
                                                "position": {
                                                    "x": 0.0,
                                                    "y": 1.5,
                                                    "z": 3.0
                                                },
                                                "rotation": {
                                                    "x": 0.0,
                                                    "y": 1.5,
                                                    "z": 3.0,
                                                    "w": 3.0
                                                },
                                                "checkpoint": {
                                                    "targetCheckpointIndex": 3,
                                                    "totalCheckpointCount": 8,
                                                    "targetCheckpointName": "Checkpoint_03",
                                                    "targetCheckpointStatus": "TARGET",
                                                    "isFinalCheckpoint": false,
                                                    "distanceToTarget": 42.5,
                                                    "targetCheckpointPosition": {
                                                        "x": 120.0,
                                                        "y": 10.0,
                                                        "z": 220.0
                                                    }
                                                }
                                            }
                                        ]
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "비행 기록 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode":"200 OK",
                                "message":"비행이 기록되었습니다."
                            }
                            """
                    )
            )
    )
    ResponseEntity<ApiResult<Void>> recordFlight(
            FlightRecordRequest request
    );

    @Operation(
            summary = "비행 리플레이 조회",
            description = "저장된 시계열 비행 기록을 리플레이용으로 조회하는 API",
            parameters = {
                    @Parameter(name = "flightId", description = "비행 ID")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "비행 리플레이 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode": "200 OK",
                                "message": "비행 리플레이가 조회되었습니다.",
                                "data": {
                                    "flightId": 1,
                                    "orderId": 1,
                                    "startedAt": "2026-06-18T05:41:44.688244100Z",
                                    "finishedAt": null,
                                    "flightStatus": "IN_FLIGHT",
                                    "samples": [
                                        {
                                            "sequence": 1,
                                            "timestampUnixMs": 178175000000,
                                            "elapsedTimeSeconds": 0.011,
                                            "position": {
                                                "x": 0.0,
                                                "y": 1.5,
                                                "z": 3.0
                                            },
                                            "rotation": {
                                                "x": 0.0,
                                                "y": 1.5,
                                                "z": 3.0,
                                                "w": 3.0
                                            }
                                        }
                                    ]
                                }
                            }
                            """
                    )
            )
    )
    ResponseEntity<ApiResult<FlightReplayResponse>> getReplay(Long flightId);


    @Operation(
            summary = "충돌 발생",
            description = "비행 중 충돌을 설정하는 API",
            parameters = {
                    @Parameter(name = "flightId", description = "비행 ID")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "비행 충돌 발생",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode":"200 OK",
                                "message":"비행이 종료되었습니다."
                            }
                            """
                    )
            )
    )
    ResponseEntity<ApiResult<Void>> setCrashed(@PathVariable Long flightId);

    @Operation(
            summary = "비행 완료",
            description = "비행이 완료 시 호출하는 API",
            parameters = {
                    @Parameter(name = "flightId", description = "비행 ID")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "비행 완료",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode": "200 OK",
                                "message": "비행이 종료되었습니다.",
                                "data": {
                                    "rank": 1,
                                    "orderId": 1,
                                    "flightId": 1,
                                    "userName": "테스트유저",
                                    "angleScore": 48.1,
                                    "timeScore": 49.4,
                                    "totalScore": 97.5,
                                    "totalFlightTime": 3,
                                    "averageTilt": 1.895,
                                    "orderStatus": "COMPLETED"
                                }
                            }
                            """
                    )
            )
    )
    ResponseEntity<ApiResult<LeaderboardResultResponse>> flightComplete(@PathVariable Long flightId);

}
