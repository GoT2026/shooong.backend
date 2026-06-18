package io.rapa.shooongbackend.ranking.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.flight.controller.FlightController;
import io.rapa.shooongbackend.flight.dto.FlightRecordRequest;
import io.rapa.shooongbackend.flight.dto.FlightRecordVo;
import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import io.rapa.shooongbackend.flight.dto.postion.RotationRequest;
import io.rapa.shooongbackend.member.entity.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.order.repository.OrderRepository;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class RankingControllerTest {

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

    String BASE_URL = "/api/rankings";
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
    void 랭킹_조회_성공() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(
                        testingUserDetails,
                        null,
                        "ROLE_USER"
                )
        );

        Long flightId = flightController.startFlight(testOrder.getOrderId())
                .getBody()
                .getData().flightId();

        List<FlightRecordVo> lst = new ArrayList<>();
        lst.add(
                new FlightRecordVo(
                        1L,
                        2000L,
                        0.1,
                        new PositionRequest(0.0, 0.0, 0.0),
                        new RotationRequest(0.0, 0.0, 0.0, 1.0)
                )
        );
        lst.add(
                new FlightRecordVo(
                        2L,
                        3000L,
                        1.1,
                        new PositionRequest(1.0, 1.0, 1.0),
                        new RotationRequest(0.0, 0.0, 0.0, 1.0)
                )
        );

        FlightRecordRequest request = new FlightRecordRequest(
                flightId,
                lst
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/flights/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/flights/{flightId}/complete", flightId)
        ).andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL)
                        .param("courseId", "1")
                        .param("limit", "10")
        ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.RANKING_RETRIEVE.getDescription()))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].rank").value(1))
                .andExpect(jsonPath("$.data[0].orderId").value(testOrder.getOrderId()))
                .andExpect(jsonPath("$.data[0].angleScore").value(50.0))
                .andExpect(jsonPath("$.data[0].timeScore").value(49.6))
                .andExpect(jsonPath("$.data[0].totalScore").value(99.6))
                .andExpect(jsonPath("$.data[0].totalFlightTime").value(2))
                .andExpect(jsonPath("$.data[0].averageTilt").value(0.0))
                .andExpect(jsonPath("$.data[0].orderStatus").value("COMPLETED"));
    }
}
