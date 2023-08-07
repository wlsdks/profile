package com.jinan.profile.service.board;

import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.repository.board.BoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[Board] - 서비스 레이어 테스트")
@Transactional
@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;


    @DisplayName("유저가 작성한 게시글을 저장한다.")
    @Test
    void test() {
        //given
        User user = createUser();
        Board board = createBoard(user);

        //when
        BoardDto boardDto = boardService.saveBoard(board);

        //then
        assertThat(boardDto).isNotNull();
        assertThat(boardDto).hasFieldOrPropertyWithValue("title", "테스트 게시글");

    }

    private Board createBoard(User user) {
        return Board.of("테스트 게시글",
                "테스트",
                10,
                20,
                user,
                List.of()
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