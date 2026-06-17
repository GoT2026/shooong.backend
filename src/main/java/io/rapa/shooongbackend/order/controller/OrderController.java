package io.rapa.shooongbackend.order.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.order.service.OrderService;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController implements OrderSwaggerSupporter {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResult<Void>> createOrder(
            @AuthenticationPrincipal DefaultCurrentUser currentUser
    ){
        orderService.createOrder(currentUser.getUserId());
        return ApiResult.empty(
                SuccessCode.ORDER_CREATE_SUCCESS
        );
    }
}
