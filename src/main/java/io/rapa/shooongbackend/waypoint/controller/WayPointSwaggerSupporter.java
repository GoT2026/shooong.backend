package io.rapa.shooongbackend.waypoint.controller;

import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.waypoint.dto.WayPointCreateRequest;
import io.rapa.shooongbackend.waypoint.entity.WayPoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(
        name = "WayPoint",
        description = "WayPoint 관련 API"
)
public interface WayPointSwaggerSupporter {
    @Operation(
            summary = "WayPoint 생성",
            description = "주문의 WayPoint를 생성하는 API",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @RequestBody(
            content = @Content(
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "orderId" : 1,
                                        "position" : {
                                            "x" : 0.1,
                                            "y" : 0.2,
                                            "z" : 0.3
                                        }
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "201",
            description = "WayPoint 생성 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode":"201 CREATED",
                                "message":"체크포인트가 생성되었습니다."
                            }
                            """
                    )
            )
    )
    ResponseEntity<ApiResult<Void>> create(WayPointCreateRequest request);


    @Operation(
            summary = "WayPoint 조회",
            description = "WayPoint를 조회하는 API",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "orderId", description = "주문 ID")
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = "WayPoint 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode":"200 OK",
                                "message":"체크포인트가 통과되었습니다.",
                                "data": [
                                    {
                                        ""
                                    }
                                ]
                            }
                            """
                    )
            )
    )
    ResponseEntity<ApiResult<List<WayPointDetailResponse>>> getWayPoints(Long orderId);

    @Operation(
            summary = "WayPoint 통과",
            description = "WayPoint 통과 처리하는 API",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "wayPointId", description = "체크포인트 ID")
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = "WayPoint 통과 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                              "statusCode": "200 OK",
                              "message": "체크포인트가 조회되었습니다.",
                              "data": [
                                {
                                  "wayPointId": 1,
                                  "position": {
                                    "posX": 0.1,
                                    "posY": 0.2,
                                    "posZ": 0.3
                                  },
                                  "passedTime": null
                                }
                              ]
                            }
                            """
                    )
            )
    )
    ResponseEntity<ApiResult<Void>> passed(Long wayPointId);
}
