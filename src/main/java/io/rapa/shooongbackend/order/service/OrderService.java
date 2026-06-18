package io.rapa.shooongbackend.order.service;

import io.rapa.shooongbackend.member.entity.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.order.dto.OrderDetailsResponse;
import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void createOrder(Long memberId){
        Members founded = memberRepository.findByIdOrThrow(memberId);
        orderRepository.save(
                Orders.builder()
                        .member(founded)
                        .build()
        );
    }

    @PreAuthorize("isAuthenticated()")
    public List<OrderDetailsResponse> getOrderDetails(Long memberId){
        Members founded = memberRepository.findByIdOrThrow(memberId);
        List<Orders> orders = orderRepository.findAllByMember(founded);
        return orders.stream()
                .map((order)-> OrderDetailsResponse.from(order))
                .toList();
    }


}
