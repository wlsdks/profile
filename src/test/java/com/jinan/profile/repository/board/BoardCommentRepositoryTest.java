package com.jinan.profile.repository.board;

import com.jinan.profile.config.TotalTestSupport;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.board.BoardComment;
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
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("[BoardCommentRepository] - 댓글 리포지토리 테스트")
class BoardCommentRepositoryTest extends TotalTestSupport {

    @Autowired private BoardCommentRepository boardCommentRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private UserRepository userRepository;

    @DisplayName("게시글id와 pageable객체를 통해 조회하면 Page형태의 댓글 엔티티를 반환받는다.")
    @Test
    void findAllCommentsByBoardId() {

        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

        User savedUser = createUser();
        Board savedBoard = createBoard(savedUser, "test");
        BoardComment savedBoardComment = createBoardComment(savedBoard, savedUser);

        //when
        Page<BoardComment> comments = boardCommentRepository.findAllCommentsByBoardId(savedBoard.getId(), pageable);

        //then
        assertThat(comments.getContent()).isNotEmpty(); // Page 객체의 내용 검증
        assertThat(comments.getTotalPages()).isGreaterThan(0); // Page 객체의 전체 페이지 수 검증
        assertThat(comments.getTotalElements()).isGreaterThan(0); // Page 객체의 전체 항목 수 검증
        assertThat(comments.getNumber()).isEqualTo(0); // Page 객체의 현재 페이지 번호 검증
        assertThat(comments.getSize()).isEqualTo(10); // Page 객체의 페이지 크기를 검증
//        assertThat(comments.hasNext()).isTrue();
        assertThat(comments.hasPrevious()).isFalse(); // 첫 번째 페이지라 이전 페이지는 없다.
        assertThat(comments.getContent().get(0).getBoard().getId()).isEqualTo(savedBoard.getId());

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