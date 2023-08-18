package com.jinan.profile.controller.board;

import com.jinan.profile.controller.board.request.BoardSubCommentRequest;
import com.jinan.profile.controller.board.response.BoardSubCommentResponse;
import com.jinan.profile.service.board.BoardSubCommentService;
import com.jinan.profile.service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/subcomment/ajax")
@Controller
public class BoardSubCommentController {

    private final BoardSubCommentService boardSubCommentService;
    private final SecurityService securityService;
//    private final PaginationService paginationService;

    /**
     * [CREATE]
     * 대댓글을 저장하는 컨트롤러
     */
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<?> saveBoardSubComment(@RequestBody BoardSubCommentRequest request) {

        String loginId = securityService.getCurrentUsername();
        boardSubCommentService.saveBoardSubComment(request.getBoardSubCommentId(), loginId, request.getContent());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * [READ]
     * 각 게시글별로 달린 대댓글을 리스트로 받아온다.
     * 동작: ajax로 호출하며 댓글 하단에 있는 댓글보기 버튼을 클릭하면 이 컨트롤러로 대댓글을 받아온다.
     */
    @ResponseBody
    @GetMapping("/get/{boardId}")
    public ResponseEntity<?> getBoardSubCommentList(
            @PathVariable Long boardId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // 대댓글을 받아서 응답값을 response 객체로 변환한다.
        Page<BoardSubCommentResponse> subCommentResponses = boardSubCommentService.getBoardSubCommentList(boardId, pageable)
                .map(BoardSubCommentResponse::fromDto);

        return new ResponseEntity<>(subCommentResponses, HttpStatus.OK);
    }

    /**
     * [UPDATE]
     * 대댓글 작성자는 본인이 작성한 대댓글을 수정할 수 있다.
     */
    @PostMapping("/update")
    public ResponseEntity<?> updateBoardSubComment(@RequestBody BoardSubCommentRequest request) {

        String loginId = securityService.getCurrentUsername();
        boardSubCommentService.updateBoardSubComment(request, loginId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * [DELETE]
     * 대댓글 작성자 본인이 작성한 대댓글을 삭제하는 기능
     */
    @DeleteMapping("/delete/{boardSubCommentId}")
    public ResponseEntity<?> deleteBoardSubComment(@PathVariable Long boardSubCommentId) {

        String loginId = securityService.getCurrentUsername();
        boardSubCommentService.deleteBoardSubComment(boardSubCommentId, loginId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
