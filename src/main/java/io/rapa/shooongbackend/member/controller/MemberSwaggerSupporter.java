package io.rapa.shooongbackend.member.controller;

import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.member.dto.UserCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Member",
        description = "계정 관련 API"
)
public interface MemberSwaggerSupporter {

    @Operation(
            summary = "회원가입",
            description = "계정의 회원가입을 수행하는 API"
    )
    @RequestBody(
            content = @Content(
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "id" : "testUser",
                                        "password" : "abcd1234",
                                        "name" : "테스트유저"
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "201",
            description = "회원가입 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode" : "201 CREATED",
                                "message":"회원가입이 성공되었습니다."
                            }
                            """
                    )
            )
    )
    ResponseEntity<ApiResult<Void>> signUp(UserCreateRequest request);
}
