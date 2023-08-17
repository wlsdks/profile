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

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("대댓글 리포지토리 테스트")
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