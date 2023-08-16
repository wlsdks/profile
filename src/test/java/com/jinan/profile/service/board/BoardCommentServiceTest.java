package com.jinan.profile.service.board;

import com.jinan.profile.config.TotalTestSupport;
import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.controller.user.request.UserRequest;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.repository.board.BoardCommentRepository;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[Comment] - 댓글 서비스 테스트")
class BoardCommentServiceTest extends TotalTestSupport {

    @Autowired private BoardCommentService boardCommentService;
    @Autowired private UserRepository userRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private BoardCommentRepository boardCommentRepository;

    @DisplayName("[happy]-유저가 댓글을 작성하고 저장버튼을 눌렀을때 댓글이 저장되는지 검증한다.")
    @Test
    void createComment() {
        //given
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser, "test");
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        BoardCommentRequest request = Optional.of(savedBoardComment)
                .map(BoardCommentDto::fromEntity)
                .map(BoardCommentRequest::fromDto)
                .get();

        //when
        boardCommentService.createComment(request, savedBoard.getId(), savedUser.getLoginId());
        Optional<BoardComment> resultComment = boardCommentRepository.findById(savedBoardComment.getId());

        //then
        assertThat(resultComment).isPresent(); // 댓글이 저장되었는지 확인
        assertThat(request.getContent()).isEqualTo(resultComment.get().getContent());
    }

    @DisplayName("[bad]-존재하지 않는 유저가 댓글을 작성하고 저장버튼을 눌렀을때 댓글 저장은 실패한다.")
    @Test
    void createCommentException() {
        //given
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser, "test");
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        BoardCommentRequest request = Optional.of(savedBoardComment)
                .map(BoardCommentDto::fromEntity)
                .map(BoardCommentRequest::fromDto)
                .get();

        //when & then
        assertThatThrownBy(() -> boardCommentService.createComment(request, savedBoard.getId(), "digggggggg"))
                .isInstanceOf(ProfileApplicationException.class);
    }

    @DisplayName("[happy]-게시글의 id(pk)를 통해 게시글과 연관된 모든 댓글정보를 조회한다.")
    @Test
    void getBoardComment() {
        //given
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser, "test");
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        Pageable pageable = PageRequest.of(1, 10);

        //when
        Page<BoardCommentDto> actual = boardCommentService.getBoardComment(savedBoard.getId(), pageable);

        //then
        assertThat(actual).isNotNull();
    }

    @DisplayName("[bad]-존재하지 않는 게시글의 id(pk)를 통해 게시글과 연관된 모든 댓글정보를 조회하면 실패한다.")
    @Test
    void getBoardCommentException() {
        //given
        Pageable pageable = PageRequest.of(1, 10);

        //when & then
        assertThatThrownBy(() -> boardCommentService.getBoardComment(100L, pageable))
                .isInstanceOf(ProfileApplicationException.class);
    }

    @DisplayName("[happy]-댓글id(pk)와 로그인한 유저의id(loginId)를 받아서 댓글 작성자가 맞는지 검증한다.")
    @Test
    void isValidCommentAuthor() {
        //given
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser, "test");
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        //when
        boolean result = boardCommentService.isValidCommentAuthor(savedBoardComment.getId(), savedUser.getLoginId());

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("[bad]-댓글id(pk)와 로그인한 유저의id(loginId)가 다르다면 검증은 실패한다.")
    @Test
    void isValidCommentAuthorException() {
        //given
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser, "test");
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        //then
        assertThatThrownBy(() -> boardCommentService.isValidCommentAuthor(savedBoardComment.getId(), "dig04058"))
                .isInstanceOf(ProfileApplicationException.class);
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