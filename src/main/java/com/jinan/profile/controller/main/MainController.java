package com.jinan.profile.controller.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/main")
@RequiredArgsConstructor
@Controller
public class MainController {

    @GetMapping("rootPage")
    public String rootPage() {

        return "rootPage";
    }
}
