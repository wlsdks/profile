package com.jinan.profile.controller.board;

import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.controller.board.response.BoardResponse;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.service.UserService;
import com.jinan.profile.service.board.BoardService;
import com.jinan.profile.service.pagination.PaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
<<<<<<< Updated upstream
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
=======
import org.springframework.security.core.context.SecurityContextHolder;
>>>>>>> Stashed changes
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
    public String createBoard(@RequestBody BoardRequest request, Principal principal) {
        // 현재 인증된 사용자의 loginId 가져오기
        String loginId = principal.getName();

        // 사용자 로그인id를 사용하여 사용자의 전체 정보 가져오기 (예: 서비스 또는 리포지토리에서)
        User user = Optional.ofNullable(userService.findByLoginId(loginId))
                .map(User::of)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        // 사용자 정보를 request에 추가
        request.setUser(user);

        boardService.createBoard(request);
        return "redirect:/board/list";
    }

    /**
     * 게시글 수정할때 유저를 검증하는 ajax기능을 위한 컨트롤러
     */
    @GetMapping("/update/validUser")
    @ResponseBody
    public ResponseEntity<String> validUpdateUser(Long boardId, Authentication authentication) {
        String loginId = authentication.getName();
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
     */
    @PostMapping("/updateBoard/{boardId}")
    public String updateBoard(@RequestBody BoardRequest request, @PathVariable Long boardId) {
        boardService.updateBoard(request, boardId);
        return "redirect:/board/list";
    }


    /**
     * READ - 게시글 단건 조회
     */
    @GetMapping("/{boardId}")
    public String selectBoard(@PathVariable Long boardId, Model model) {
        BoardDto boardDto = boardService.selectBoard(boardId);

        model.addAttribute("boardId", boardId);
        model.addAttribute("board", boardDto);

        return "/board/detail";
    }

    /**
     * READ - 로그인 id로 모든 게시글 조회
     */
    @ResponseBody
    @GetMapping("/getAllBoard")
    public void selectAllBoardByLoginId(String loginId) {
        boardService.findByLoginId(loginId);
    }

}
