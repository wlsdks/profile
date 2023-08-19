package com.jinan.profile.repository.board;

import com.jinan.profile.config.TotalTestSupport;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("게시글 repository 테스트")
class BoardRepositoryTest extends TotalTestSupport {

    @Autowired private BoardRepository boardRepository;
    @Autowired private UserRepository userRepository;

    @DisplayName("유저의 로그인id로 모든 게시글을 리스트로 조회한다.")
    @Test
    void findByLoginId() {
        //given
        User user = createUser();
        Board board = createBoard(user, "테스트");
        User savedUser = userRepository.save(user);
        Board savedBoard = boardRepository.save(board);

        //when
        List<Board> boardList = boardRepository.findByLoginId(savedUser.getLoginId())
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        //then
        assertThat(boardList).isEqualTo(List.of(board));
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0)).isInstanceOf(Board.class);
    }

    @DisplayName("존재하지 않는 로그인id로 모든 게시글을 조회하면 빈 Optional<List<>> 객체를 반환한다.")
    @Test
    void findByLoginIdException() {
        //given
        String loginId = "anonymousId";

        //when
        Optional<List<Board>> boardList = boardRepository.findByLoginId(loginId);

        //then
        assertThat(boardList).isInstanceOf(Optional.class);
    }


    private Board createBoard(User user, String title) {
        return Board.of(title,
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