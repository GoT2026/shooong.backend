package io.rapa.shooongbackend.security.dto;

public record UserLoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
