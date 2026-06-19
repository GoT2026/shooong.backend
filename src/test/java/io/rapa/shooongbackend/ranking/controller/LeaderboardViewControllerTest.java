package io.rapa.shooongbackend.ranking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class LeaderboardViewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void 리더보드_화면_조회_성공() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/leaderboard")
        ).andExpect(status().isOk())
                .andExpect(view().name("leaderboard"))
                .andExpect(content().string(containsString("비행 결과와 전체 랭킹")))
                .andExpect(content().string(containsString("리더보드")));
    }
}
