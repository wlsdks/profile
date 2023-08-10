package com.jinan.profile.service.board;

import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.controller.board.response.BoardResponse;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시글을 저장한다. -> 게시글을 작성한 유저는 무조건 있어야 한다.
     */
    @Transactional
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
     * paging을 적용시켜서 10개씩 페이지에 보여주도록 했다.
     */
    public Page<BoardDto> selectAllBoardList(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(BoardDto::fromEntity);
    }

    /**
     * 게시글의 id를 통해 게시글 정보를 가져온다.
     */
    public BoardDto findById(Long boardId) {
        return boardRepository.findById(boardId)
                .map(BoardDto::fromEntity)
                .orElseThrow(
                        () -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND)
                );
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void updateBoard(BoardRequest request, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        board.change(request);
    }


}
