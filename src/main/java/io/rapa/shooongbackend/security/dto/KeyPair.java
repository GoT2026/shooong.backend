package io.rapa.shooongbackend.security.dto;

public record KeyPair(
        String accessToken,
        String refreshToken
) {
}
