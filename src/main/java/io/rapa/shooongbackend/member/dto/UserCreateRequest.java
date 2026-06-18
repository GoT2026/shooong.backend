package io.rapa.shooongbackend.member.dto;

public record UserCreateRequest(
        String id,
        String password,
        String name
) {
}
