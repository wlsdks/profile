package com.jinan.profile.service.board;

import com.jinan.profile.domain.board.Board;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시글을 저장한다.
     */
    public BoardDto saveBoard(Board board) {
        return Optional.of(boardRepository.save(board))
                .map(BoardDto::fromEntity)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.INSERT_ERROR));
    }

}
