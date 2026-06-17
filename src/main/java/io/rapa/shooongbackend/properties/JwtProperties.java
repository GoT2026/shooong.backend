package io.rapa.shooongbackend.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(value = "custom")
@RequiredArgsConstructor
public class JwtProperties {
    private String appKey;

    private Validations validations;

    @Getter
    @RequiredArgsConstructor
    private static class Validations{
        private Long access;
        private Long refresh;
    }
}
