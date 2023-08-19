package com.jinan.profile.controller.board;

import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.controller.board.response.BoardCommentResponse;
import com.jinan.profile.controller.board.response.BoardResponse;
import com.jinan.profile.controller.board.response.BoardSubCommentResponse;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.service.UserService;
import com.jinan.profile.service.board.BoardCommentService;
import com.jinan.profile.service.board.BoardService;
import com.jinan.profile.service.board.BoardSubCommentService;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final PaginationService paginationService;
    private final SecurityService securityService;
    private final BoardCommentService boardCommentService;
    private final BoardSubCommentService boardSubCommentService;

    /**
     * READ - 게시글 리스트 뷰 이동
     * 모든 게시글을 조회해서 리스트에 보여준다.
     */
    @GetMapping("/list")
    public String boardListView(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {

        Page<BoardResponse> boardResponses = boardService.selectAllBoardList(pageable).map(BoardResponse::fromDto);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), boardResponses.getTotalPages());

        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("boardList", boardResponses.getContent()); // getContent()로 실제 목록을 가져옵니다.
        map.addAttribute("totalPages", boardResponses.getTotalPages());
        map.addAttribute("currentPage", pageable.getPageNumber());

        return "/board/list";
    }

    /**
     * CREATE - 게시글 작성 뷰 이동
     */
    @GetMapping("/create")
    public String boardCreateView(Model model) {
        return "/board/create";
    }

    /**
     * Action - 게시글 저장기능
     * request로 받아온 데이터를 db에 저장한다.
     */
    @PostMapping("/createBoard")
    public String createBoard(@RequestBody BoardRequest request) {

        String loginId = securityService.getCurrentUsername();

        User user = Optional.ofNullable(userService.findByLoginId(loginId))
                .map(User::of)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        request.setUser(user);
        boardService.createBoard(request);

        return "redirect:/board/list";
    }

    /**
     * 게시글 수정할때 유저를 검증하는 ajax기능을 위한 컨트롤러
     * 지금은 사용하고 있지 않지만 이걸 util클래스로 빼낸다음 게시글 이외에도 유저 검증이 필요한 곳에서 사용할 수 있을것으로 보인다.
     */
    @GetMapping("/update/validUser")
    @ResponseBody
    public ResponseEntity<String> validUpdateUser(Long boardId) {

        String loginId = securityService.getCurrentUsername();

        // 유저 검증 로직 작성
        boolean validUser = boardService.validUser(boardId, loginId);
        if (validUser) {
            // 검증된 유저인 경우, 200 OK 응답 반환
            return ResponseEntity.ok("검증된 유저입니다.");
        }
        // 검증되지 않은 유저인 경우, 400 Bad Request 응답 반환
        return ResponseEntity.status(ErrorCode.NOT_VALID_ERROR.getStatus()).body("검증되지 않은 유저입니다.");
    }


    /**
     * UPDATE - 게시글 수정 뷰 이동
     * 게시글을 작성한 유저 본인만 수정 뷰로 이동하도록 한다.
     * javaScript에서 먼저 ajax로 valid 코드를 호출한다. 이후 통과하면 아래의 메서드가 동작한다.
     */
    @GetMapping("/update/{boardId}")
    public String updateBoardView(@PathVariable Long boardId, Model model) {

        // 해당 id의 게시글을 찾는다.
        BoardResponse boardResponse = Optional.of(boardService.findById(boardId))
                .map(BoardResponse::fromDto)
                .get();


        model.addAttribute("boardResponse", boardResponse);
        model.addAttribute("boardId", boardId);
        return "/board/update";
    }

    /**
     * action - 게시글 업데이트
     * 게시글을 올린 유저인지 인증을 거쳐서 업데이트를 가능하도록 한다.
     */
    @PostMapping("/updateBoard/{boardId}")
    public String updateBoard(
            @RequestBody BoardRequest request,
            @PathVariable Long boardId,
            Principal principal
    ) {

        String loginId = principal.getName();
        BoardDto boardDto = boardService.updateBoard(request, boardId, loginId);
        return "redirect:/board/list";
    }


//    /**
//     * READ - 게시글 단건 조회
//     */
//    @GetMapping("/{boardId}")
//    public String selectBoard(@PathVariable Long boardId, Model model) {
//        BoardDto boardDto = boardService.selectBoard(boardId);
//
//        model.addAttribute("boardId", boardId);
//        model.addAttribute("board", boardDto);
//
//        return "/board/detail";
//    }

    /**
     * READ - 게시글 단건 조회
     */
    @GetMapping("/{boardId}")
    public String boardDetail(
            @PathVariable Long boardId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        // 게시글 상세 정보 가져오기
        BoardResponse boardResponse = BoardResponse.fromDto(boardService.selectBoard(boardId));
        map.addAttribute("board", boardResponse);

        // 댓글 목록 가져오기
        Page<BoardCommentResponse> boardComment = boardCommentService.getBoardComment(boardId, pageable)
                .map(BoardCommentResponse::fromDto);

        map.addAttribute("boardComment", boardComment);

        // 대댓글 목록 가져오기 (옵션: 필요한 경우)
        Page<BoardSubCommentResponse> boardSubComment = boardSubCommentService.getBoardSubCommentList(boardId, pageable)
                .map(BoardSubCommentResponse::fromDto);

        map.addAttribute("boardSubComment", boardSubComment);

        return "board/detail";  // 타임리프 템플릿 경로
    }


    /**
     * READ - 로그인 id로 모든 게시글 조회
     * 이게 필요한지 한번 더 확인해보자
     */
    @ResponseBody
    @GetMapping("/getAllBoard")
    public ResponseEntity<?> selectAllBoardByLoginId(String loginId) {
        boardService.findByLoginId(loginId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
