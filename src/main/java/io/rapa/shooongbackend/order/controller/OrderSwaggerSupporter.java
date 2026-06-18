package io.rapa.shooongbackend.order.controller;


import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.order.dto.OrderDetailsResponse;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(
        name = "Order",
        description = "주문 관련 API"
)
public interface OrderSwaggerSupporter {


    @Operation(
            summary = "주문 생성",
            description = "계정의 주문을 생성하는 API",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "201",
            description = "주문 생성 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode" : "201 CREATED",
                                "message" : "주문이 생성되었습니다."
                            }
                            """
                    )
            )
    )
    ResponseEntity<ApiResult<Void>> createOrder(@Parameter(hidden = true) DefaultCurrentUser currentUser);


    @Operation(
            summary = "주문조회",
            description = "계정의 주문을 조회하는 API",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "주문 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode" : "200 OK",
                                "message" : "주문이 조회되었습니다.",
                                "data" : [
                                    {
                                        "orderId":2,
                                        "rating":null,
                                        "score":null,
                                        "totalFlightTime":null,
                                        "averageTilt":null,
                                        "orderStatus":"PROCESSING",
                                        "remainWaypointCnt":0
                                    }
                                ]
                            }
                            """
                    )
            )
    )
    ResponseEntity<ApiResult<List<OrderDetailsResponse>>> getOrders(@Parameter(hidden = true) DefaultCurrentUser currentUser);
}
