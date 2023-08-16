package com.jinan.profile.controller.board;

import com.jinan.profile.service.board.BoardSubCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/subcomment/")
@Controller
public class BoardSubCommentController {

    private final BoardSubCommentService boardSubCommentService;

    @GetMapping("/getSubCommentList")
    public String getSubCommentList() {

        boardSubCommentService.getSubCommentList();
        return "";
    }


}
