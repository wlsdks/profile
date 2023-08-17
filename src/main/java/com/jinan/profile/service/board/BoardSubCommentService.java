package com.jinan.profile.service.board;

import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.domain.board.BoardSubComment;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.dto.board.BoardSubCommentDto;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardCommentRepository;
import com.jinan.profile.repository.board.BoardSubCommentRepository;
import com.jinan.profile.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardSubCommentService {
    private final BoardSubCommentRepository boardSubCommentRepository;

    private final UserRepository userRepository;
    private final BoardCommentRepository boardCommentRepository;

    public void getSubCommentList() {

    }

    /**
     * 대댓글을 저장한다.
     */
    @Transactional
    public BoardSubCommentDto saveBoardSubComment(Long boardCommentId, String loginId, String content) {

        // 1. 댓글정보를 가져온다.
        BoardComment boardComment = boardCommentRepository.findById(boardCommentId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.BOARD_COMMENT_NOT_FOUND));

        // 2. 유저정보를 가져온다.
        User user = userRepository.findUserByLoginId(loginId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        // 3. 저장할 대댓글 엔티티를 생성한다. 대댓글을 적게될 상위 댓글 엔티티와 대댓글을 작성한 유저와 대댓글 내용을 통해 만들어 준다.
        BoardSubComment boardSubComment = BoardSubComment.of(boardComment, user, content);

        return BoardSubCommentDto.fromEntity(boardSubCommentRepository.save(boardSubComment));
    }
}
