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
     * READ - 게시글 리스트 뷰 이동
     * 모든 게시글을 조회해서 리스트에 보여준다.
     */
    @GetMapping("/list")
    public String getList(Model model) {

        List<BoardDto> boardDtoList = boardService.selectAllBoardList();

        model.addAttribute("boardList", boardDtoList);
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
    @ResponseBody
    @PostMapping("/createBoard")
    public void createBoard(@RequestBody BoardRequest request) {
        boardService.createBoard(request);
    }

    /**
     * READ - 게시글 단건 조회
     */
    @ResponseBody
    @GetMapping("/{boardId}")
    public void selectBoard(@PathVariable Long boardId) {
        boardService.selectBoard(boardId);
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
