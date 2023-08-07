package com.jinan.profile.config;

import com.jinan.profile.controller.chat.ChatMainController;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Profile;

@Profile("test")
@AutoConfigureMockMvc
@WebMvcTest({
        ChatMainController.class
})
public abstract class ControllerTestSupport {

}
