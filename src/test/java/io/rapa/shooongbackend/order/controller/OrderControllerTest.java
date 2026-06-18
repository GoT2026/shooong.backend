package io.rapa.shooongbackend.order.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.member.entity.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.order.service.OrderService;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    OrderService orderService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ObjectMapper objectMapper;


    String BASE_URL = "/api/orders";
    String TEST_USER_ID = "wjdtn3902";
    String TEST_USER_PW = "wjd747";

    UserDetails testingUserDetails;

    Members testMember;

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
    }

    @Test
    void 주문_생성_성공() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .with(SecurityMockMvcRequestPostProcessors.user(testingUserDetails))
        ).andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.ORDER_CREATE_SUCCESS.getDescription()));
    }

    @Test
    void 주문_조회_성공() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(
                testingUserDetails,
                null,
                "ROLE_USER"
        )
        );

        orderService.createOrder(testMember.getMemberId());

        mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL)
                ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.ORDER_RETRIEVE_SUCCESS.getDescription()));
    }
}