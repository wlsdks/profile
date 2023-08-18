package com.jinan.profile.controller;

import com.jinan.profile.config.ControllerTestSupport;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("실시간 채팅 controller 테스트")
class ChatMainControllerTest extends ControllerTestSupport {


    public ChatMainControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }


}