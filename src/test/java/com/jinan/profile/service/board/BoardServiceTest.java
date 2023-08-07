package com.jinan.profile.service.board;

import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("[Board] - 서비스 레이어 테스트")
@Transactional
@SpringBootTest
class BoardServiceTest {

    @Autowired private BoardService boardService;
    @Autowired private BoardRepository boardRepository;
    @Autowired private UserRepository userRepository;

    @DisplayName("유저가 작성한 게시글을 저장한다.")
    @Test
    void saveBoard() {
        //given
        User user = createUser();
        Board board = createBoard(user);

        //when
        BoardDto boardDto = boardService.saveBoard(board);

        //then
        assertThat(boardDto).isNotNull();
        assertThat(boardDto).hasFieldOrPropertyWithValue("title", "테스트 게시글");
        assertThat(boardDto).hasFieldOrPropertyWithValue("content", "테스트");
        assertThat(boardDto.title()).isEqualTo("테스트 게시글");
        assertThat(boardDto.content()).isEqualTo("테스트");
        assertThat(boardDto).isInstanceOf(BoardDto.class);


        // 만약 컬렉션이 있다면
//        assertThat(boardDto.getComments()).hasSize(0);
//        assertThat(boardDto.getComments()).contains(commentDto);
//        assertThat(boardDto.getComments()).isEmpty();
    }

    @DisplayName("유저가 지정되지 않은 게시글을 저장하면 예외가 발생한다.")
    @Test
    void saveBoardUserNotFoundException() {
        //given
        User user = createUser();
        Board board = createBoard(null);

        //when & then
        assertThatThrownBy(() -> boardService.saveBoard(board))
                .isInstanceOf(ProfileApplicationException.class);

    }

    @DisplayName("유저의 로그인id를 통해 유저가 작성한 게시글을 조회한다.")
    @Test
    void findByUserId() {
        //given
        User user = createUser();
        Board board1 = createBoard(user, "테스트1");
        Board board2 = createBoard(user, "테스트2");
        Board board3 = createBoard(user, "테스트3");

        userRepository.save(user);
        List<Board> boards = boardRepository.saveAll(List.of(board1, board2, board3));

        //when
        List<BoardDto> boardList = boardService.findByUserId(user.getLoginId());

        //then
        assertThat(boardList.get(0)).isInstanceOf(BoardDto.class);
        assertThat(boardList).hasSize(3);

    }

    @DisplayName("존재하지않는 유저의 로그인id로 게시글을 조회하면 예외가 발생한다.")
    @Test
    void findByUserIdExceptionCase() {
        //given
        String loginId = "anonymous";

        //when & then
        assertThatThrownBy(() -> boardService.findByUserId(loginId))
                .isInstanceOf(ProfileApplicationException.class);
    }

    @DisplayName("조회하고싶은 게시글을 클릭하면 그 게시글의 id로 게시글 단건을 조회한다.")
    @Test
    void selectBoard() {
        //given
        User user = createUser();
        Board board = createBoard(user);
        BoardDto savedBoard = boardService.saveBoard(board);

        //when
        BoardDto actual = boardService.selectBoard(board.getId());

        //then
        assertThat(actual).isEqualTo(savedBoard);
        assertThat(actual).isInstanceOf(BoardDto.class);

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

    private Board createBoard(User user, String title) {
        return Board.of(title,
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