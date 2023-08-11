package com.jinan.profile.service.board;

import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.repository.board.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * 유저가 댓글을 작성하면 저장된다.
     */
    public BoardCommentDto createComment(BoardCommentRequest request) {

        // todo: fromRequest에서 getId를 할때 request가 null이라고 나온다. request안에있는 BoardRequest가 null이다.
        BoardComment boardComment = Optional.of(request)
                .map(BoardCommentDto::fromRequest)
                .map(BoardComment::fromDto)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        return BoardCommentDto.fromEntity(commentRepository.save(boardComment));
    }

}
