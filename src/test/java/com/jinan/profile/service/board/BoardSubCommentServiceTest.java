package com.jinan.profile.service.board;

import com.jinan.profile.config.TotalTestSupport;
import com.jinan.profile.controller.board.request.BoardSubCommentRequest;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.domain.board.BoardSubComment;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.board.BoardSubCommentDto;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardCommentRepository;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.repository.board.BoardSubCommentRepository;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("대댓글 service 테스트")
class BoardSubCommentServiceTest extends TotalTestSupport {

    @Autowired
    private BoardSubCommentService boardSubCommentService;
    @Autowired
    private BoardSubCommentRepository boardSubCommentRepository;
    @Autowired
    private BoardCommentRepository boardCommentRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;


    @DisplayName("[happy] - 사용자가 작성된 댓글에 대댓글을 작성해서 저장한다.")
    @Test
    void saveBoardSubComment() {
        //given
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser);
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        Long boardCommentId = 1L;
        String loginId = "wlsdks123";

        //when
        BoardSubCommentDto savedBoardSubCommentDto = boardSubCommentService.saveBoardSubComment(savedBoardComment.getId(), loginId, "test content");

        //then
        assertThat(savedBoardSubCommentDto).isNotNull();
        assertThat(savedBoardSubCommentDto).isInstanceOf(BoardSubCommentDto.class);
    }

    @DisplayName("[bad]-존재하지 않는 사용자가 댓글에 대댓글을 작성하면 예외가 발생한다.")
    @Test
    void saveBoardSubCommentException1() {
        //given
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser);
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        Long boardCommentId = 1L;
        String loginId = "ssssss";

        //when & then
        assertThatThrownBy(() -> boardSubCommentService.saveBoardSubComment(boardCommentId, loginId, "test content"))
                .isInstanceOf(ProfileApplicationException.class);
    }

    @DisplayName("[bad]-존재하지 않는 댓글에 사용자가 대댓글을 작성하면 예외가 발생한다.")
    @Test
    void saveBoardSubCommentException2() {
        //given
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser);
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        // 2L -> board_id의 댓글은 존재하지 않는다.
        Long boardCommentId = 100L;
        String loginId = "wlsdks123";

        //when & then
        assertThatThrownBy(() -> boardSubCommentService.saveBoardSubComment(boardCommentId, loginId, "test content"))
                .isInstanceOf(ProfileApplicationException.class);
    }

    @DisplayName("[bad]-대댓글에 null이거나 공백 문자를 적어서 저장했을때는 예외가 발생한다.")
    @Test
    void saveBoardSubCommentException3() {
        //given
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser);
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        Long boardCommentId = 1L;
        String loginId = "wlsdks123";

        //when & then
        //junit5의 테스트인 assertAll과 assertJ의 assertThatThrownBy를 함께 사용했다.
        assertAll(
            () -> assertThatThrownBy(() -> boardSubCommentService.saveBoardSubComment(boardCommentId, loginId, ""))
                    .isInstanceOf(ProfileApplicationException.class),

            () -> assertThatThrownBy(() -> boardSubCommentService.saveBoardSubComment(boardCommentId, loginId, null))
                    .isInstanceOf(ProfileApplicationException.class)
        );
    }

    @DisplayName("[happy]-특정 게시글이 가진 대댓글을 조회한다.")
    @Test
    void getBoardSubCommentList() {
        //given
        BoardSubComment savedBoardSubComment = makeSavedBoardSubComment();
        Long boardId = savedBoardSubComment.getBoardComment().getBoard().getId();
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<BoardSubCommentDto> boardSubCommentList = boardSubCommentService.getBoardSubCommentList(boardId, pageable);

        //then
        assertThat(savedBoardSubComment).isNotNull();
        assertThat(boardSubCommentList).isNotNull();
        assertThat(boardSubCommentList).hasSize(1);
        assertThat(boardSubCommentList).isInstanceOf(Page.class);
    }

    @DisplayName("[happy]-대댓글 작성자가 대댓글을 수정하면 적용된다.")
    @Test
    void updateBoardSubComment() {
        //given
        BoardSubComment savedBoardSubComment = makeSavedBoardSubComment();
        BoardSubCommentDto boardSubCommentDto = BoardSubCommentDto.fromEntity(savedBoardSubComment);
        BoardSubCommentRequest request = BoardSubCommentRequest.fromDto(boardSubCommentDto);
        String loginId = savedBoardSubComment.getUser().getLoginId();

        //when
        boardSubCommentService.updateBoardSubComment(request, loginId);

        //then
        BoardSubComment updatedBoardSubComment = boardSubCommentRepository.findById(request.getBoardSubCommentId()).orElseThrow();
        assertThat(updatedBoardSubComment.getContent()).isEqualTo(request.getContent());
    }

    @DisplayName("[bad]-대댓글 작성자가 아닌 유저가 대댓글을 수정하면 예외가 발생한다.")
    @Test
    void updateBoardSubCommentException() {
        //given
        BoardSubComment savedBoardSubComment = makeSavedBoardSubComment();

        Long boardSubCommentId = savedBoardSubComment.getId();
        BoardSubCommentRequest request = createBoardSubCommentRequest(boardSubCommentId);

        String loginId = savedBoardSubComment.getUser().getLoginId();

        //when
        boardSubCommentService.updateBoardSubComment(request, loginId);

        //then
        BoardSubComment updatedBoardSubComment = boardSubCommentRepository
                .findById(request.getBoardSubCommentId()).orElseThrow();

        assertThat(updatedBoardSubComment.getContent()).isEqualTo(request.getContent());
    }

    private BoardSubCommentRequest createBoardSubCommentRequest(long boardSubCommentId) {
        return BoardSubCommentRequest.of(
                boardSubCommentId, "test", LocalDateTime.now(), LocalDateTime.now()
        );
    }

    // 유저, 게시글, 댓글, 대댓글을 모두 영속화시키는 method이다.
    private BoardSubComment makeSavedBoardSubComment() {
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser);
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

    private Board createBoard(User user) {
        Board board = Board.of(
                "test",
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