package io.rapa.shooongbackend.security.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.member.entity.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.security.dto.UserLoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
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
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ObjectMapper objectMapper;


    String BASE_URL = "/api/auth";
    String TEST_USER_ID = "wjdtn3902";
    String TEST_USER_PW = "wjd747";

    @BeforeEach
    void setUp(){
        memberRepository.save(
                Members.builder()
                        .name("이정수")
                        .loginId(TEST_USER_ID)
                        .password(passwordEncoder.encode(TEST_USER_PW))
                        .build()
        );
    }

    @Test
    void 로그인_테스트_성공() throws Exception {

        UserLoginRequest request = new UserLoginRequest(
                TEST_USER_ID,
                TEST_USER_PW
        );

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.LOGIN_SUCCESS.getDescription()));
    }

}