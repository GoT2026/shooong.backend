package io.rapa.shooongbackend.common.dto;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.exception.CustomException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@Slf4j
@RestControllerAdvice
public class ApiAdvice {
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiErrorResponse> handlerException(Exception e){
//        return ApiErrorResponse.toResponseEntity(
//                ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),
//                ErrorCode.INTERNAL_SERVER_ERROR.getDescription()
//        );
//    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomException(
            CustomException customException
    ){
        return ApiErrorResponse.toResponseEntity(
                customException.getErrorCode().getStatus(),
                customException.getErrorCode().getDescription()
        );
    }
}
