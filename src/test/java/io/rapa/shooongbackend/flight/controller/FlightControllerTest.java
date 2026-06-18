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
                        MockMvcRequestBuilders.post(BASE_URL + "/record")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.FLIGHT_RECORD.getDescription()));
    }

    @Test
    void 비행_리플레이_조회() throws Exception {
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
                        0.1,
                        new PositionRequest(2.0, 3.0, 4.0),
                        new RotationRequest(0.0, 0.0, 0.0, 1.0)
                )
        );
        lst.add(
                new FlightRecordVo(
                        2L,
                        2100L,
                        0.2,
                        new PositionRequest(5.0, 6.0, 7.0),
                        new RotationRequest(0.0, 0.1, 0.0, 1.0)
                )
        );

        FlightRecordRequest request = new FlightRecordRequest(
                flightId,
                lst
        );

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL + "/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/{flightId}/replay", flightId)
        ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.FLIGHT_REPLAY_RETRIEVE.getDescription()))
                .andExpect(jsonPath("$.data.flightId").value(flightId))
                .andExpect(jsonPath("$.data.orderId").value(testOrder.getOrderId()))
                .andExpect(jsonPath("$.data.samples[0].sequence").value(1))
                .andExpect(jsonPath("$.data.samples[0].position.x").value(2.0))
                .andExpect(jsonPath("$.data.samples[1].sequence").value(2));
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
                        MockMvcRequestBuilders.post(BASE_URL + "/record")
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
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.FLIGHT_FINISHED.getDescription()))
                .andExpect(jsonPath("$.data.rank").value(1))
                .andExpect(jsonPath("$.data.orderId").value(testOrder.getOrderId()))
                .andExpect(jsonPath("$.data.flightId").value(flightId))
                .andExpect(jsonPath("$.data.angleScore").value(50.0))
                .andExpect(jsonPath("$.data.timeScore").value(50.0))
                .andExpect(jsonPath("$.data.totalScore").value(100.0))
                .andExpect(jsonPath("$.data.totalFlightTime").value(0))
                .andExpect(jsonPath("$.data.averageTilt").value(0.0))
                .andExpect(jsonPath("$.data.orderStatus").value("COMPLETED"));


        Flights founded = flightRepository.findByIdOrThrow(flightId);

        Assertions.assertThat(founded.getFlightStatus()).isEqualTo(FlightStatus.FLIGHT_COMPLETE);
        Assertions.assertThat(testOrder.getRating()).isEqualTo(1);
        Assertions.assertThat(testOrder.getScore()).isEqualTo(100.0);
        Assertions.assertThat(testOrder.getTotalFlightTime()).isEqualTo(0L);
        Assertions.assertThat(testOrder.getAverageTilt()).isEqualTo(0.0);
    }

    @Test
    void 이분_비행_시뮬레이션_점수_계산() throws Exception {
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

        RotationRequest rotation = eulerToQuaternion(
                6.0,
                4.0,
                0.0
        );

        List<FlightRecordVo> records = new ArrayList<>();
        for (int i = 0; i <= 240; i++) {
            double elapsedTimeSeconds = i * 0.5;
            records.add(
                    new FlightRecordVo(
                            (long) i + 1,
                            1_000_000L + Math.round(elapsedTimeSeconds * 1000),
                            elapsedTimeSeconds,
                            new PositionRequest(
                                    elapsedTimeSeconds,
                                    20.0,
                                    elapsedTimeSeconds * 2
                            ),
                            rotation
                    )
            );
        }

        FlightRecordRequest request = new FlightRecordRequest(
                flightId,
                records
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL + "/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_URL + "/{flightId}/complete", flightId)
        ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.angleScore").value(45.0))
                .andExpect(jsonPath("$.data.timeScore").value(26.0))
                .andExpect(jsonPath("$.data.totalScore").value(71.0))
                .andExpect(jsonPath("$.data.totalFlightTime").value(120))
                .andExpect(jsonPath("$.data.averageTilt").value(5.0));

        Assertions.assertThat(testOrder.getRating()).isEqualTo(1);
        Assertions.assertThat(testOrder.getScore()).isEqualTo(71.0);
        Assertions.assertThat(testOrder.getTotalFlightTime()).isEqualTo(120L);
        Assertions.assertThat(testOrder.getAverageTilt()).isEqualTo(5.0);
    }

    private RotationRequest eulerToQuaternion(
            double rollDegrees,
            double pitchDegrees,
            double yawDegrees
    ) {
        double roll = Math.toRadians(rollDegrees);
        double pitch = Math.toRadians(pitchDegrees);
        double yaw = Math.toRadians(yawDegrees);

        double cy = Math.cos(yaw * 0.5);
        double sy = Math.sin(yaw * 0.5);
        double cp = Math.cos(pitch * 0.5);
        double sp = Math.sin(pitch * 0.5);
        double cr = Math.cos(roll * 0.5);
        double sr = Math.sin(roll * 0.5);

        return new RotationRequest(
                sr * cp * cy - cr * sp * sy,
                cr * sp * cy + sr * cp * sy,
                cr * cp * sy - sr * sp * cy,
                cr * cp * cy + sr * sp * sy
        );
    }
}
