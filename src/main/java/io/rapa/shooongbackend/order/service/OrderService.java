package io.rapa.shooongbackend.order.service;

import io.rapa.shooongbackend.member.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
