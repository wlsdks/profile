package com.jinan.profile.controller.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/main")
@Controller
public class ChatMainController {


    @GetMapping("/page")
    public String rootPage() {
        return "chatPage";
    }

}
