package com.jinan.profile.controller.board;

import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.controller.board.response.BoardCommentResponse;
import com.jinan.profile.service.board.BoardCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/board/comment")
@RequiredArgsConstructor
@Controller
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    /**
     * 게시글에 해당하는 댓글 조회
     * 고민인게 이걸 ajax로 호출해서 아래에 뿌리도록 할까? 아니면 게시글을 조회할때 한번에 데이터를 동시에 가져오도록 할까?
     */
    @ResponseBody
    @GetMapping("/get/{boardId}")
    public List<BoardCommentResponse> getComment(@PathVariable Long boardId) {
        return boardCommentService.getBoardComment(boardId)
                .stream()
                .map(BoardCommentResponse::fromDto)
                .toList();
    }

    /**
     * 댓글 저장하기 ajax로 저장기능 동작
     */
    @ResponseBody
    @PostMapping("/create/{boardId}") // boardId를 경로 변수로 받습니다.
    public ResponseEntity<?> createComment(
            @RequestBody BoardCommentRequest request,
            @PathVariable Long boardId,
            Principal principal
    ) { // @PathVariable 어노테이션 추가

        String loginId = principal.getName();
        boardCommentService.createComment(request, boardId, loginId);
        return ResponseEntity.ok("댓글 저장에 성공했습니다.");
    }



}
