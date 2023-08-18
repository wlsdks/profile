package com.jinan.profile.repository.board;

import com.jinan.profile.config.TotalTestSupport;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.domain.board.BoardSubComment;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

class BoardSubCommentRepositoryTest extends TotalTestSupport {

    @Autowired private BoardSubCommentRepository boardSubCommentRepository;
    @Autowired private BoardCommentRepository boardCommentRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private UserRepository userRepository;

    @DisplayName("[happy] - 대댓글을 저장하면 저장된 대댓글을 반환한다.")
    @Test
    void saveSubComments() {
        //given
        // 영속화되지 않은 엔터티를 설정하면, 테스트 실행 시 TransientObjectException이나 관련 예외가 발생할 수 있다. 그러니 save메서드를 생성 메서드안에서 실행한다.
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser, "test");
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);
        BoardSubComment boardSubComment = BoardSubComment.of(savedBoardComment, savedUser, "test");

        //when
        BoardSubComment result = boardSubCommentRepository.save(boardSubComment);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(BoardSubComment.class);
        assertThat(result.getContent()).isEqualTo("test");

    }

    @Transactional(readOnly = true)
    @DisplayName("[happy] - 특정 게시글이 가진 대댓글을 조회한다.")
    @Test
    void findAllById() {
        //given
        BoardSubComment savedBoardSubComment = makeSavedBoardSubComment();
        Long boardId = savedBoardSubComment.getBoardComment().getBoard().getId();
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<BoardSubComment> result = boardSubCommentRepository.findAllByBoardId(boardId, pageable);

        //then
        assertThat(savedBoardSubComment).isNotNull();
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1); // 현재 페이지의 항목 수
        assertThat(result.getTotalPages()).isEqualTo(1); // 전체 페이지 수
        assertThat(result.getTotalElements()).isEqualTo(1); // 전체 항목 수
        assertThat(result.getNumber()).isEqualTo(0); // 현재 페이지 번호
        assertThat(result.getSize()).isEqualTo(10); // 페이지 당 항목 수
    }


    // 유저, 게시글, 댓글, 대댓글을 모두 영속화시키는 method이다.
    private BoardSubComment makeSavedBoardSubComment() {
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser, "test");
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);
        return createBoardSubComment(savedUser, savedBoardComment);
    }

    private BoardSubComment createBoardSubComment(User savedUser, BoardComment savedBoardComment) {
        BoardSubComment boardSubComment = BoardSubComment.of(
                savedBoardComment, savedUser, "test"
        );
        return boardSubCommentRepository.save(boardSubComment);
    }

    private BoardComment createBoardComment(Board board, User user) {
        BoardComment comment = BoardComment.of(
                board,
                user,
                "내용"
        );
        return boardCommentRepository.save(comment);
    }

    private Board createBoard(User user, String title) {
        Board board = Board.of(
                title,
                "테스트",
                10,
                20,
                user
        );
        return boardRepository.save(board);
    }

    private User createUser() {
        User user = User.of(
                "wlsdks123",
                "wlsdks12",
                "wlsdks",
                "wlsdks12@naver.com",
                RoleType.ADMIN,
                UserStatus.Y
        );
        return userRepository.save(user);
    }


}