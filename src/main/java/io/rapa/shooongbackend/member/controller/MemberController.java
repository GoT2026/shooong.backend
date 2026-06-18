package io.rapa.shooongbackend.member.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.member.service.MemberService;
import io.rapa.shooongbackend.member.dto.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController implements MemberSwaggerSupporter {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResult<Void>> signUp(
            @RequestBody UserCreateRequest request
    ){
        memberService.signUp(request);
        return ApiResult.empty(
                SuccessCode.USER_CREATE_SUCCESS
        );
    }
}
