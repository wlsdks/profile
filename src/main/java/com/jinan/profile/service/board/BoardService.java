package com.jinan.profile.service.board;

import com.jinan.profile.controller.board.request.BoardRequest;
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
    public BoardDto createBoard(BoardRequest request) {
        // 1. request에서 board 엔티티로 변환해 준다.
        Board board = Board.toRequest(request);

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
    public List<BoardDto> findByLoginId(String loginId) {
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

    /**
     * 존재하는 모든 게시글을 가져온다.
     */
    public List<BoardDto> selectAllBoardList() {
        return boardRepository.findAll()
                .stream()
                .map(BoardDto::fromEntity)
                .collect(Collectors.toList());
    }

}
