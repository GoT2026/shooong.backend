package io.rapa.shooongbackend.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiErrorResponse {
    private final String code;
    private final String message;
    public static ResponseEntity<ApiErrorResponse> toResponseEntity(
            HttpStatus httpStatus,
            String message
    ){
        return ResponseEntity.status(httpStatus)
                .body(
                        new ApiErrorResponse(
                                httpStatus.toString(),
                                message
                        )
                );
    }
}
