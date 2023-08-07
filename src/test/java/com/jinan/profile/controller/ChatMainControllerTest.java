package com.jinan.profile.controller;

import com.jinan.profile.config.ControllerTestSupport;
import com.jinan.profile.controller.chat.ChatMainController;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@DisplayName("[컨트롤러] - 메인화면")
class ChatMainControllerTest extends ControllerTestSupport {

    private MockMvc mockMvc;
    @MockBean private UserRepository userRepository;


    public ChatMainControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }


}