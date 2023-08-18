package com.jinan.profile.controller.board;

import com.jinan.profile.controller.board.request.BoardSubCommentRequest;
import com.jinan.profile.controller.board.response.BoardSubCommentResponse;
import com.jinan.profile.service.board.BoardSubCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/subcomment")
@Controller
public class BoardSubCommentController {

    private final BoardSubCommentService boardSubCommentService;
//    private final PaginationService paginationService;

    /**
     * [CREATE]
     * 대댓글을 저장하는 컨트롤러
     */
    @ResponseBody
    @PostMapping("/action/save")
    public String saveBoardSubComment(
            @RequestBody BoardSubCommentRequest request,
            Principal principal
    ) {

        String loginId = principal.getName();
        boardSubCommentService.saveBoardSubComment(request.getBoardSubCommentId(), loginId, request.getContent());
        return "/";
    }

    /**
     * [READ]
     * 각각의 게시글에 달린 대댓글을 리스트로 받아오는 컨트롤러
     * ajax로 호출한다.
     */
    @ResponseBody
    @GetMapping("/getBoardSubCommentList/{boardId}")
    public Page<BoardSubCommentResponse> getBoardSubCommentList(
            @PathVariable Long boardId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // 대댓글을 받아서 응답값을 response 객체로 변환한다.
        return boardSubCommentService.getBoardSubCommentList(boardId, pageable)
                .map(BoardSubCommentResponse::fromDto);
    }


}
