package com.jinan.profile.service.board;

import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardDto saveBoard() {

        return null;
    }

}
