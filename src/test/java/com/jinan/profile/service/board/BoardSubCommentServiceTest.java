package com.jinan.profile.service.board;

import com.jinan.profile.config.TotalTestSupport;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.domain.board.BoardSubComment;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.board.BoardSubCommentDto;
import com.jinan.profile.repository.board.BoardCommentRepository;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.repository.board.BoardSubCommentRepository;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("[BoardSubComment] - 리포지토리 테스트")
class BoardSubCommentServiceTest extends TotalTestSupport {

    @Autowired private BoardSubCommentService boardSubCommentService;
    @Autowired private BoardSubCommentRepository boardSubCommentRepository;
    @Autowired private BoardCommentRepository boardCommentRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private UserRepository userRepository;


    @DisplayName("사용자가 작성된 댓글에 대댓글을 작성해서 저장한다.")
    @Test
    void saveBoardSubComment() {
        //given
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser, "test");
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        Long boardCommentId = 1L;
        String loginId = "wlsdks123";

        //when
        BoardSubCommentDto savedBoardSubCommentDto = boardSubCommentService.saveBoardSubComment(boardCommentId, loginId, "test content");

        //then
        assertThat(savedBoardSubCommentDto).isNotNull();
        assertThat(savedBoardSubCommentDto).isInstanceOf(BoardSubCommentDto.class);
    }




    private BoardSubComment createBoardSubComment(User savedUser, BoardComment savedBoardComment) {
        return BoardSubComment.of(
                savedBoardComment, savedUser, "test"
        );
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