package com.jinan.profile.controller.board;

import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.controller.board.response.BoardCommentResponse;
import com.jinan.profile.controller.board.response.BoardResponse;
import com.jinan.profile.controller.board.response.BoardSubCommentResponse;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardCommentRepository;
import com.jinan.profile.service.board.BoardCommentService;
import com.jinan.profile.service.pagination.PaginationService;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/board/comment")
@RequiredArgsConstructor
@Controller
public class BoardCommentController {

    private final BoardCommentService boardCommentService;
    private final PaginationService paginationService;
    private final SecurityService securityService;

    /**
     * CREATE - 댓글 저장하기 ajax로 저장기능 동작
     */
    @ResponseBody
    @PostMapping("/create/{boardId}") // boardId를 경로 변수로 받습니다.
    public ResponseEntity<?> createComment(
            @RequestBody BoardCommentRequest request,
            @PathVariable Long boardId
    ) {
        String loginId = securityService.getCurrentUsername();
        BoardCommentResponse boardCommentResponse = BoardCommentResponse.fromDto(boardCommentService.createComment(request, boardId, loginId));

        // 댓글 정보를 응답합니다.
        Map<String, Object> response = new HashMap<>();
        response.put("id", boardCommentResponse.getBoardCommentId()); // 댓글 ID를 추가
        response.put("username", boardCommentResponse.getUserResponse().getUsername());
        response.put("content", boardCommentResponse.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * READ - 게시글에 해당하는 댓글 조회
     */
    @ResponseBody
    @GetMapping("/get/{boardId}")
    public ResponseEntity<?> getComment(
            @PathVariable Long boardId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // 페이징된 데이터 세팅
        Page<BoardCommentResponse> BoardCommentResponses = boardCommentService.getBoardComment(boardId, pageable)
                .map(BoardCommentResponse::fromDto);

        paginationService.getPaginationBarNumbers(pageable.getPageNumber(), BoardCommentResponses.getTotalPages());

        return new ResponseEntity<>(BoardCommentResponses, HttpStatus.OK);
    }

    /**
     * UPDATE - 댓글을 수정한다.
     * content, commentId를 받아와서 서비스에서 변경감지로 업데이트를 처리한다.
     */
    @ResponseBody
    @PostMapping("/update/{commentId}")
    public ResponseEntity<?> updateBoardComment(
            @RequestBody BoardCommentRequest request, // 여기로 data로 보낸값이 바인딩되어 들어온다.
            @PathVariable Long commentId
    ) {

        String loginId = securityService.getCurrentUsername();
        boardCommentService.updateBoardComment(request, commentId, loginId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * DELETE - 댓글 삭제
     */
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {

        String loginId = securityService.getCurrentUsername();
        boardCommentService.deleteComment(commentId, loginId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * VALID - 댓글 작성자를 검증하는 로직
     * 본인일때만 작성한 댓글을 수정하거나 삭제할 수 있다.
     */
    @ResponseBody
    @GetMapping("/validComment")
    public ResponseEntity<?> validComment(@RequestParam Long commentId) {

        String loginId = securityService.getCurrentUsername();
        boolean isValid = boardCommentService.isValidCommentAuthor(commentId, loginId);

        if (!isValid) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
