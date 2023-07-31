package com.jinan.profile.controller;

import com.jinan.profile.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[컨트롤러] - 메인화면")
@Profile("test")
@AutoConfigureMockMvc // 이걸 달아줘야 MockMvc에 주입이 된다.
@Import(TestSecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {

    private MockMvc mockMvc;

    public MainControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @WithMockUser
    @Test
    @DisplayName("메인화면 응답 테스트")
    void test1() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/main/page"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

}