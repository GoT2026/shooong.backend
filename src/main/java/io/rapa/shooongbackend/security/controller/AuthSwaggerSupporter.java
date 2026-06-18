package io.rapa.shooongbackend.security.controller;

import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.security.dto.UserLoginRequest;
import io.rapa.shooongbackend.security.dto.UserLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Authentication",
        description = "인증 관련 API"
)
public interface AuthSwaggerSupporter {

    @Operation(
            summary = "로그인",
            description = "계정의 로그인을 수행하는 API"
    )
    @RequestBody(
            content = @Content(
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "userId" : "testUser",
                                        "password" : "abcd1234"
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode" : "200 OK",
                                "message" : "로그인이 성공되었습니다.",
                                "data" : {
                                    "accessToken" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzgxNjk4NTYwLCJleHAiOjE3ODE3MDAzNjB9.uJLXO5mzRm5mjjiNCLBDfex1m4E_dIav4nqctuy_OUJBaqhsiSzTXornf7Kq1qSq8m-gUHN9rpgX9ia8FOmiGw",
                                    "refreshToken" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzgxNjk4NTYwLCJleHAiOjE3ODIzMDMzNjB9.rlMc70aC13NwTn5znM2_fONoIDGj--jeF45ir4Qn0oDgR1ItReAipKULqEsdjOdKS6k6ellSaFN69J08acu3SA",
                                    "tokenType":"Bearer"
                                }
                            }
                            """
                    )
        )
    )
    ResponseEntity<ApiResult<UserLoginResponse>> signIn(
            UserLoginRequest request
    );
}
