package com.jinan.profile.controller.board;

import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.service.board.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/board/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;

    /**
     * 게시글에 해당하는 댓글 조회
     */
    @GetMapping("/get")
    public String getComment() {
        return null;
    }

    /**
     * 댓글 저장하기 ajax로 저장기능 동작
     */
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody BoardCommentRequest request) {
        commentService.createComment(request);
        return ResponseEntity.ok("댓글 저장에 성공했습니다.");
    }


}
