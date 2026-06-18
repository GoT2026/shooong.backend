package io.rapa.shooongbackend.flight.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.flight.constant.FlightStatus;
import io.rapa.shooongbackend.flight.dto.FlightRecordRequest;
import io.rapa.shooongbackend.flight.dto.FlightRecordVo;
import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import io.rapa.shooongbackend.flight.dto.postion.RotationRequest;
import io.rapa.shooongbackend.flight.entity.Flights;
import io.rapa.shooongbackend.flight.repository.FlightRepository;
import io.rapa.shooongbackend.member.entity.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.order.repository.OrderRepository;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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

@Slf4j
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
    @Autowired
    private FlightRepository flightRepository;

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

    @Test
    void 비행_기록_실패() throws  Exception{
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

        Flights founded = flightRepository.findByIdOrThrow(flightId);
        founded.setCrashed();

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
                ).andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void 비행_충돌_발생() throws Exception {
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

        mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL + "/{flightId}/crash", flightId)
                ).andExpect(status().isOk())
                .andDo(print());


        Flights founded = flightRepository.findByIdOrThrow(flightId);

        Assertions.assertThat(founded.getFlightStatus()).isEqualTo(FlightStatus.CRASHED);
    }

    @Test
    void 비행_완료() throws Exception {
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

        mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL + "/{flightId}/complete", flightId)
                ).andExpect(status().isOk())
                .andDo(print());


        Flights founded = flightRepository.findByIdOrThrow(flightId);

        Assertions.assertThat(founded.getFlightStatus()).isEqualTo(FlightStatus.FLIGHT_COMPLETE);
    }
}