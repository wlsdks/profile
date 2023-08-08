package com.jinan.profile.controller.board;

import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/save")
    @ResponseBody
    public void getBoard(@RequestBody BoardRequest request) {
        boardService.saveBoard(request);
    }

}
