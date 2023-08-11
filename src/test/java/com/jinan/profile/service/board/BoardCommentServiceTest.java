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
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.repository.board.BoardCommentRepository;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Comment] - 댓글 서비스 테스트")
class BoardCommentServiceTest extends TotalTestSupport {

    @Autowired private BoardCommentService boardCommentService;
    @Autowired private UserRepository userRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private BoardCommentRepository boardCommentRepository;

    @DisplayName("유저가 댓글을 작성하고 저장버튼을 눌렀을때 댓글이 저장되는지 검증한다.")
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

        request.setBoardRequest(BoardRequest.fromEntity(savedBoard));
        request.setUserRequest(UserRequest.fromEntity(savedUser));

        //when
        BoardCommentDto actual = boardCommentService.createComment(request, savedBoard.getId());

        //then
        assertThat(actual).isNotNull();
        assertThat(actual).isInstanceOf(BoardCommentDto.class);
    }

    @DisplayName("게시글의 id를 통해 게시글과 연관된 모든 댓글정보를 조회한다.")
    @Test
    void getBoardComment() {
        //given
        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser, "test");
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        //when
        List<BoardCommentDto> actual = boardCommentService.getBoardComment(savedBoard.getId());

        //then
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(BoardCommentDto.fromEntity(savedBoardComment));

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
                "wlsdks12",
                "wlsdks12",
                "wlsdks",
                "wlsdks12@naver.com",
                RoleType.ADMIN,
                UserStatus.Y
        );
        return userRepository.save(user);
    }

}