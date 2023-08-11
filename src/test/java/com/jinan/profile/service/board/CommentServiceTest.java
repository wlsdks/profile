package com.jinan.profile.service.board;

import com.jinan.profile.config.TotalTestSupport;
import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.board.BoardCommentDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Comment] - 댓글 서비스 테스트")
class CommentServiceTest extends TotalTestSupport {

    private CommentService commentService;

    @DisplayName("유저가 댓글을 작성하면 저장된다.")
    @Test
    void test() {
        //given
        User user = createUser();
        Board board = createBoard(user, "test");
        BoardComment boardComment = createBoardComment(board, user);

        BoardCommentRequest request = Optional.of(boardComment)
                .map(BoardCommentDto::fromEntity)
                .map(BoardCommentRequest::fromDto)
                .get();

        //when
        BoardCommentDto actual = commentService.createComment(request);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(BoardCommentDto.fromEntity(boardComment));
        assertThat(actual).isInstanceOf(BoardCommentDto.class);
    }

    private BoardComment createBoardComment(Board board, User user) {
        return BoardComment.of(
                board,
                user,
                "내용"
        );
    }

    private Board createBoard(User user, String title) {
        return Board.of(
                title,
                "테스트",
                10,
                20,
                user
        );
    }

    private User createUser() {
        return User.of(
                "wlsdks12",
                "wlsdks12",
                "wlsdks",
                "wlsdks12@naver.com",
                RoleType.ADMIN,
                UserStatus.Y
        );
    }

}