package com.jinan.profile.service.board;

import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardDto saveBoard() {

        return BoardDto.of(
                1L,
                "테스트 게시글",
                "내용이다.",
                20,
                20,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

}
