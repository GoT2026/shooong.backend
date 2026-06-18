package io.rapa.shooongbackend.flight.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.flight.dto.FlightRecordRequest;
import io.rapa.shooongbackend.flight.dto.FlightRecordVo;
import io.rapa.shooongbackend.flight.dto.StartFlightResponse;
import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import io.rapa.shooongbackend.flight.dto.postion.RotationRequest;
import io.rapa.shooongbackend.member.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.order.repository.OrderRepository;
import io.rapa.shooongbackend.order.service.OrderService;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class FlightControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    FlightController flightController;
    @Autowired
    ObjectMapper objectMapper;

    String BASE_URL = "/api/flights";
    String TEST_USER_ID = "wjdtn3902";
    String TEST_USER_PW = "wjd747";

    UserDetails testingUserDetails;

    Members testMember;
    Orders testOrder;

    @BeforeEach
    void setUp(){
        testMember = memberRepository.save(
                Members.builder()
                        .name("이정수")
                        .loginId(TEST_USER_ID)
                        .password(passwordEncoder.encode(TEST_USER_PW))
                        .build()
        );
        testingUserDetails = DefaultCurrentUser.from(testMember);
        testOrder = orderRepository.save(
                Orders.builder()
                        .member(testMember)
                        .build()
        );


    }

    @Test
    void 비행_시작() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/{orderId}", testOrder.getOrderId())
                                .with(SecurityMockMvcRequestPostProcessors.user(testingUserDetails))
                ).andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.FLIGHT_START.getDescription()));
    }

    @Test
    void 비행_기록_시작() throws  Exception{
        // given
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(
                testingUserDetails,
                null,
                "ROLE_USER"
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long flightId = flightController.startFlight(testOrder.getOrderId())
                        .getBody()
                                .getData().flightId();

        List<FlightRecordVo> lst = new ArrayList<>();
        lst.add(
                        new FlightRecordVo(
                                1L,
                                2000L,
                                230.324313,
                                new PositionRequest(2.0,2.0,2.0),
                                new RotationRequest(2.0,2.0,2.0,2.0)
                        )
                );

        FlightRecordRequest request = new FlightRecordRequest(
                flightId,
                lst
        );

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/{flightId}/record", flightId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.FLIGHT_RECORD.getDescription()));
    }
}