package io.rapa.shooongbackend.security.dto;

public record UserLoginRequest(
        String userId,
        String password
) {
}
