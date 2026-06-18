package io.rapa.shooongbackend.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    // 유저
    LOGIN_SUCCESS(HttpStatus.OK,"로그인이 성공되었습니다."),
    USER_CREATE_SUCCESS(HttpStatus.CREATED, "회원가입이 성공되었습니다."),


    // 주문
    ORDER_CREATE_SUCCESS(HttpStatus.CREATED, "주문이 생성되었습니다."),
    ORDER_RETRIEVE_SUCCESS(HttpStatus.OK, "주문이 조회되었습니다.")

    ;


    private final HttpStatus status;
    private final String description;
}
