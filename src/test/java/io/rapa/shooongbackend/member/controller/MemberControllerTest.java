package io.rapa.shooongbackend.member.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.member.dto.UserCreateRequest;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.member.service.MemberService;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ObjectMapper objectMapper;


    String BASE_URL = "/api/members";
    String TEST_USER_ID = "wjdtn3902";
    String TEST_USER_PW = "wjd747";

    @Test
    void 회원가입_성공() throws Exception {

        UserCreateRequest request = new UserCreateRequest(
                TEST_USER_ID,
                passwordEncoder.encode(TEST_USER_PW),
                "테스터"
        );

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(SuccessCode.USER_CREATE_SUCCESS.getDescription()));
    }

}