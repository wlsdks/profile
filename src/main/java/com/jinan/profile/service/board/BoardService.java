package com.jinan.profile.service.board;

import com.jinan.profile.domain.board.Board;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시글을 저장한다. -> 게시글을 작성한 유저는 무조건 있어야 한다.
     */
    public BoardDto saveBoard(Board board) {
        if (board.getUser() == null) {
            throw new ProfileApplicationException(ErrorCode.USER_NOT_FOUND);
        }
        return Optional.of(boardRepository.save(board))
                .map(BoardDto::fromEntity)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.INSERT_ERROR));
    }

    /**
     * 유저의 로그인id로 유저가 작성한 모든 게시글 리스트를 조회한다.
     */
    public List<BoardDto> findByUserId(String loginId) {
        return boardRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND))
                .stream()
                .map(BoardDto::fromEntity)
                .collect(Collectors.toList());
    }


    /**
     * 게시글 id를 통해 단건의 게시글 정보를 조회한다.
     */
    public BoardDto selectBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .map(BoardDto::fromEntity)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));
    }

}
