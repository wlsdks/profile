package com.jinan.profile.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {


    @GetMapping("/getBoard")
    public String getBoard() {


        return "/board";
    }

}
