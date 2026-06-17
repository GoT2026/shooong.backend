package io.rapa.shooongbackend.order.controller;


import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(
        name = "Order",
        description = "주문 관련 API"
)
public interface OrderSwaggerSupporter {


    @Operation(
            summary = "주문생성",
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
    ResponseEntity<ApiResult<Void>> createOrder(DefaultCurrentUser currentUser);
}
