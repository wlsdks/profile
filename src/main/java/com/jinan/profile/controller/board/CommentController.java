package com.jinan.profile.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequestMapping("/board/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    /**
     * 게시글에 해당하는 댓글 조회
     */
    @GetMapping("/get")
    public String getComment() {
        return null;
    }

    /**
     * 댓글 추가
     */
    @ResponseBody
    @PostMapping("/create")
    public void createComment() {

    }


}
