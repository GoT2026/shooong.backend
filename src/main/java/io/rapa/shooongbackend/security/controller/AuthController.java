package io.rapa.shooongbackend.security.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.security.dto.KeyPair;
import io.rapa.shooongbackend.security.dto.UserLoginRequest;
import io.rapa.shooongbackend.security.dto.UserLoginResponse;
import io.rapa.shooongbackend.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthSwaggerSupporter {
    private final AuthService authService;
    private final String TOKEN_TYPE = "Bearer";

    @PostMapping("/login")
    public ResponseEntity<ApiResult<UserLoginResponse>> signIn(
        @RequestBody UserLoginRequest request
    ){
        KeyPair keyPair = authService.signIn(request);
        return ApiResult.data(
                SuccessCode.LOGIN_SUCCESS,
                new UserLoginResponse(
                        keyPair.accessToken(),
                        keyPair.refreshToken(),
                        TOKEN_TYPE
                )
        );
    }
}
