package com.jinan.profile.service.board;

import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;

    /**
     * 유저가 댓글을 작성하면 저장된다.
     */
    public BoardCommentDto createComment(BoardCommentRequest request) {

        BoardComment boardComment = Optional.of(request)
                .map(BoardCommentDto::fromRequest)
                .map(BoardComment::fromDto)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        return BoardCommentDto.fromEntity(boardCommentRepository.save(boardComment));
    }

    /**
     * 게시글의 id를 통해 연관된 모든 댓글을 가져온다.
     */
    public List<BoardCommentDto> getBoardComment(Long boardId) {

        return boardCommentRepository.findAllCommentsByBoardId(boardId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.BAD_REQUEST_ERROR))
                .stream()
                .map(BoardCommentDto::fromEntity)
                .collect(Collectors.toList());
    }



}
