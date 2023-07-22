package com.jinan.profile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/main")
@Controller
public class MainController {

    @GetMapping("/page")
    public String rootPage() {
        return "mainPage";
    }

}
