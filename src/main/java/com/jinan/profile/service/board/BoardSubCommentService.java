package com.jinan.profile.service.board;

import com.jinan.profile.controller.board.request.BoardSubCommentRequest;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.domain.board.BoardSubComment;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.dto.board.BoardSubCommentDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardCommentRepository;
import com.jinan.profile.repository.board.BoardSubCommentRepository;
import com.jinan.profile.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardSubCommentService {

    private final BoardSubCommentRepository boardSubCommentRepository;
    private final UserRepository userRepository;
    private final BoardCommentRepository boardCommentRepository;


    /**
     * CREATE - 대댓글 저장
     * 대댓글pk, 저장하려는 유저의 id, 대댓글 내용을 param으로 받아서 데이터를 가공 후에 저장한다.
     */
    @Transactional
    public BoardSubCommentDto saveBoardSubComment(Long boardCommentId, String loginId, String content) {

        if (content == null || content.isEmpty()) {
            throw new ProfileApplicationException(ErrorCode.NOT_VALID_BOARD_SUB_COMMENT_CONTENT);
        }

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

    /**
     * READ - 게시글이 가진 대댓글 조회
     */
    public Page<BoardSubCommentDto> getBoardSubCommentList(Long boardId, Pageable pageable) {
        return boardSubCommentRepository.findAllByBoardId(boardId, pageable)
                .map(BoardSubCommentDto::fromEntity);
    }

    /**
     * UPDATE - 작성한 대댓글 수정(변경감지 update)
     */
    public void updateBoardSubComment(BoardSubCommentRequest request, String loginId) {

        // 1. 저장된 대댓글 엔티티를 받아온다.
        BoardSubComment boardSubComment = boardSubCommentRepository.findById(request.getBoardSubCommentId())
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.BOARD_SUB_COMMENT_NOT_FOUND));

        // 2. 유저를 확인해서 예외처리를 한다.
        String boardSubCommentUserLoginId = boardSubComment.getUser().getLoginId();
        if (!boardSubCommentUserLoginId.equals(loginId)) {
            throw new ProfileApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 3. 엔티티의 값을 변경한다.
        boardSubComment.changeContent(request);
    }


}
