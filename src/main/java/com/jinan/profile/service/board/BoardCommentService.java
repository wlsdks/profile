package com.jinan.profile.service.board;

import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardCommentRepository;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardCommentService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardCommentRepository boardCommentRepository;

    private static final int MAX_LENGTH = 2000;

    /**
     * CREATE
     * 유저가 댓글을 작성하면 저장된다.
     */
    @Transactional
    public void createComment(BoardCommentRequest request, Long boardId, String loginId) {

        // 1. boardId로 board를 꺼내서 BoardRequest에 넣어야 한다.
        BoardComment boardComment = BoardComment.of(request.getContent());

        // 2. boardComment에 board는 boardId로 받아와서 세팅한다.
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        // 3. user는 loginId로 받아와서 세팅한다.
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        // 4. user, board를 세팅한다. 엔티티가 영속화되지 않았기때문에 영속성 컨텍스트에 아직 포함되지 않았다. 즉, save()를 써줘야 한다.
        boardComment.changeUserAndBoard(user, board);
        boardCommentRepository.save(boardComment);
    }

    /**
     * READ
     * 게시글의 id를 통해 연관된 모든 댓글을 가져온다. -> 페이징 처리된 데이터를 받아온다.
     */
    public Page<BoardCommentDto> getBoardComment(Long boardId, Pageable pageable) {
        return boardCommentRepository.findAllCommentsByBoardId(boardId, pageable)
                .map(BoardCommentDto::fromEntity);
    }

    /**
     * UPDATE
     * 댓글 업데이트 로직 -> 각 단계별 검증을 거친후 업데이트 된다.
     */
    @Transactional
    public void updateBoardComment(BoardCommentRequest request, Long commentId, String loginId) {

        // 1. 일단 content의 로직을 검증한다. null, "", 최대길이 초과는 예외를 던진다.
        String content = request.getContent();
        if (content == null || content.trim().isEmpty() || content.length() > MAX_LENGTH) {
            throw new ProfileApplicationException(ErrorCode.INVALID_INPUT);
        }

        // 2. 댓글을 db에서 가져온다.
        BoardComment boardComment = boardCommentRepository.findById(commentId).
                orElseThrow(() -> new ProfileApplicationException(ErrorCode.COMMENT_NOT_FOUND));

        // 3. 만약 댓글작성한 유저랑 수정하려는 유저가 다르다면 예외를 던진다.
        String commentUserId = boardComment.getUser().getLoginId();
        if (!commentUserId.equals(loginId)) {
            throw new ProfileApplicationException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        // 4. 변경감지로 업데이트를 한다. -> 트랜잭션 종료될때 update쿼리를 날림
        boardComment.changeContent(request.getContent());
    }

    /**
     * DELETE
     * 댓글삭제 로직 -> 댓글이 가진 id와 삭제하려는 유저의 id를 비교해서 동일하다면 삭제한다.
     */
    @Transactional
    public void deleteComment(Long commentId, String loginId) {
        BoardComment comment = boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getLoginId().equals(loginId)) {
            throw new ProfileApplicationException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        boardCommentRepository.delete(comment);
    }


    /**
     * VALID
     * 댓글 작성자를 검증하는 로직 -> 댓글id(pk)와 접속한 유저id(loginId)를 받아서 같은 유저인지 검증해서 boolean값을 반환한다.
     */
    public boolean isValidCommentAuthor(Long commentId, String loginId) {
        // 1. 댓글을 가져온다.
        BoardComment comment = boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        // 2. 유저정보를 가져온다.
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        // 3. 댓글의 유저와 가져온 유저정보를 비교한다.
        return comment.getUser().equals(user);
    }


}
