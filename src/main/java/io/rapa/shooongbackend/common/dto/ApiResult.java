package io.rapa.shooongbackend.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@Hidden
public class ApiResult<T> {
    private final String statusCode;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;
    private static <T> ResponseEntity<ApiResult<T>> toResponseEntity(
            SuccessCode successCode,
            T data
            ){
        return ResponseEntity.status(successCode.getStatus())
                .body(
                        new ApiResult<T>(
                                successCode.getStatus().toString(),
                                successCode.getDescription(),
                                data
                        )
                );
    }
    public static ResponseEntity<ApiResult<Void>> empty(
            SuccessCode successCode
    ){
        return ApiResult.toResponseEntity(
                successCode,
                null
        );
    }
    public static <T> ResponseEntity<ApiResult<T>> data(
            SuccessCode successCode,
            T data
    ){
        return ApiResult.toResponseEntity(
                successCode,
                data
        );
    }
    public static <T> ResponseEntity<ApiResult<Page<T>>> page(SuccessCode successCode, Page<T> page) {
        return ResponseEntity
                .status(successCode.getStatus())
                .body(
                        new ApiResult<Page<T>>(
                                successCode.getStatus().toString(),
                                successCode.getDescription(),
                                page
                        )
                );
    }
}
