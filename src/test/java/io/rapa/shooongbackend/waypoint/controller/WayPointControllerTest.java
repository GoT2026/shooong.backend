package io.rapa.shooongbackend.waypoint.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.flight.dto.postion.PositionRequest;
import io.rapa.shooongbackend.member.entity.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.order.repository.OrderRepository;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
import io.rapa.shooongbackend.waypoint.dto.WayPointCreateRequest;
import io.rapa.shooongbackend.waypoint.entity.WayPoints;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class WayPointControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    WayPointController wayPointController;
    @Autowired
    ObjectMapper objectMapper;

    String BASE_URL = "/api/waypoints";
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
    void 웨이포인트_저장_성공() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(
                        testingUserDetails,
                        null,
                        "ROLE_USER"
                )
        );

        WayPointCreateRequest request = new WayPointCreateRequest(
                testOrder.getOrderId(),
                new PositionRequest(1.0, 1.0, 1.0)
        );

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.WAY_POINT_CREATE_SUCCESS.getDescription()));



        Assertions.assertThat(orderRepository.findByIdOrThrow(testOrder.getOrderId()).getRemainWaypointCnt()).isEqualTo(1);
    }

    @Test
    void 웨이포인트_통과_성공() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(
                        testingUserDetails,
                        null,
                        "ROLE_USER"
                )
        );

        WayPointCreateRequest request = new WayPointCreateRequest(
                testOrder.getOrderId(),
                new PositionRequest(1.0, 1.0, 1.0)
        );

        wayPointController.create(request);

        WayPointDetailResponse foundedWayPoint = wayPointController.getWayPoints(testOrder.getOrderId()).getBody().getData().get(0);
        Assertions.assertThat(foundedWayPoint).isNotNull();

        mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL + "/{wayPointId}", foundedWayPoint.wayPointId())
                ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.WAY_POINT_PASSED_SUCCESS.getDescription()));

        Assertions.assertThat(testOrder.getRemainWaypointCnt()).isEqualTo(0);
    }

}