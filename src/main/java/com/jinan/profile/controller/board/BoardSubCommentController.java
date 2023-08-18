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
    public void saveBoardSubComment(
            @RequestBody BoardSubCommentRequest request,
            Principal principal
    ) {

        String loginId = principal.getName();
        boardSubCommentService.saveBoardSubComment(request.getBoardSubCommentId(), loginId, request.getContent());
    }

    /**
     * [READ]
     * 각각의 게시글에 달린 대댓글을 리스트로 받아오는 컨트롤러
     * ajax로 호출한다. -> 동작: 댓글 하단의 댓글보기를 클릭하면 이 컨트롤러를 통해 그 댓글의 대댓글 정보를 보여준다.
     */
    @ResponseBody
    @GetMapping("/get/{boardId}")
    public Page<BoardSubCommentResponse> getBoardSubCommentList(
            @PathVariable Long boardId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // 대댓글을 받아서 응답값을 response 객체로 변환한다.
        return boardSubCommentService.getBoardSubCommentList(boardId, pageable)
                .map(BoardSubCommentResponse::fromDto);
    }

    /**
     * [UPDATE]
     * 대댓글 작성자는 본인이 작성한 대댓글을 수정할 수 있다.
     */
    @PostMapping("/action/update")
    public void updateBoardSubComment(
            @RequestBody BoardSubCommentRequest request,
            Principal principal
    ) {
        String loginId = principal.getName();
        boardSubCommentService.updateBoardSubComment(request, loginId);
    }


}
