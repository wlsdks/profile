package com.jinan.profile.controller.board;

import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {

    private final BoardService boardService;

    /**
     * READ - 게시글 리스트 이동
     */
    @GetMapping("/list")
    public String getList(Model model) {

        List<BoardDto> boardDtoList = boardService.selectAllBoardList();

        model.addAttribute("boardList", boardDtoList);
        return "/board/list";
    }

    @GetMapping("/create")
    public String boardCreate(Model model) {
        return "/board/create";
    }

    /**
     * CREATE - 게시글 저장
     */
    @ResponseBody
    @PostMapping("/save")
    public void saveBoard(@RequestBody BoardRequest request) {
        boardService.saveBoard(request);
    }

    /**
     * READ - 게시글 단건을 조회
     */
    @ResponseBody
    @GetMapping("/{boardId}")
    public void selectBoard(@PathVariable Long boardId) {
        boardService.selectBoard(boardId);
    }

    /**
     * READ - 로그인id로 모든 게시글 조회
     */
    @ResponseBody
    @GetMapping("/getAllBoard")
    public void selectAllBoardByLoginId(String loginId) {
        boardService.findByLoginId(loginId);
    }

}
