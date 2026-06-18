package io.rapa.shooongbackend.flight.controller;

import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.flight.dto.StartFlightResponse;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
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
            }
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
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<ApiResult<StartFlightResponse>> startFlight(Long orderId);

}
