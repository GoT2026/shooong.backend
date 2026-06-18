package io.rapa.shooongbackend.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 유저
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 계정을 찾을 수 없습니다."),
    AUTHENTICATION_INCORRECT(HttpStatus.UNAUTHORIZED, "로그인 정보가 올바르지 않습니다."),
    USER_ID_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "해당 아이디가 이미 존재합니다."),

    // 주문
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),



    // 비행
    ORDER_NOT_VALID(HttpStatus.BAD_REQUEST, "인증된 사용자가 주문자가 아닙니다."),
    ALREADY_ASSIGNED_ORDER(HttpStatus.BAD_REQUEST, "이미 드론이 배정된 주문입니다."),


    // 토큰
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다."),
    ABNORMAL_TOKEN(HttpStatus.UNAUTHORIZED, "정상적이지 않은 토큰입니다."),
    ERROR_FROM_TOKEN(HttpStatus.UNAUTHORIZED, "토큰에서 문제가 발생했습니다.")
    ;
    private final HttpStatus status;
    private final String description;
}
