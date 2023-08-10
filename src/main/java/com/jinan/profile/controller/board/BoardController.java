package com.jinan.profile.controller.board;

import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.dto.user.UserDto;
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
import org.springframework.security.core.context.SecurityContext;
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
    public String getList(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {

        Page<BoardDto> boardDtoList = boardService.selectAllBoardList(pageable);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), boardDtoList.getTotalPages());

        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("boardList", boardDtoList.getContent()); // getContent()로 실제 목록을 가져옵니다.
        map.addAttribute("totalPages", boardDtoList.getTotalPages());
        map.addAttribute("currentPage", pageable.getPageNumber());

        return "/board/list";
    }

    /**
     * CREATE - 게시글 작성 뷰 이동
     */
    @GetMapping("/create")
    public String boardCreate(Model model) {
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
     * READ - 게시글 단건 조회
     */
    @GetMapping("/{boardId}")
    public String selectBoard(@PathVariable Long boardId, Model model) {
        BoardDto boardDto = boardService.selectBoard(boardId);
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
