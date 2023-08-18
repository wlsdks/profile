package com.jinan.profile.controller;

import com.jinan.profile.config.ControllerTestSupport;
import com.jinan.profile.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

class ChatMainControllerTest extends ControllerTestSupport {

    private MockMvc mockMvc;
    @MockBean private UserRepository userRepository;


    public ChatMainControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }


}