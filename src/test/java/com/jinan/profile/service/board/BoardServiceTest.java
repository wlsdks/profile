package com.jinan.profile.service.board;

import com.jinan.profile.config.TotalTestSupport;
import com.jinan.profile.controller.board.request.BoardRequest;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestConfiguration
@DisplayName("[Board] - 서비스 레이어 테스트")
class BoardServiceTest extends TotalTestSupport {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("등록된 모든 게시글을 조회한다.")
    @Test
    void findAllBoardList() {
        //given
        User user = createUser("wlsdks12");
        Board board = createBoard(user);
        Board board1 = createBoard(user);
        Board board2 = createBoard(user);

        User savedUser = userRepository.save(user);
        List<BoardDto> savedBoardDtoList = boardRepository
                .saveAll(List.of(board, board1, board2))
                .stream()
                .map(BoardDto::fromEntity)
                .toList();

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<BoardDto> boardDtoList = boardService.selectAllBoardList(pageable);

        //then
        assertThat(boardDtoList).hasSize(3);
        assertThat(boardDtoList).isNotNull();

    }

    @DisplayName("유저가 작성한 게시글을 저장한다.")
    @Test
    void saveBoard() {
        //given
        User user = createUser("wlsdks12");
        Board board = createBoard(user);
        BoardRequest request = BoardRequest.fromEntity(board);

        //when
        BoardDto boardDto = boardService.createBoard(request);

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
    void createBoardUserNotFoundException() {
        //given
        User user = createUser("wlsdks12");
        Board board = createBoard(null);
        BoardRequest request = BoardRequest.fromEntity(board);

        //when & then
        assertThatThrownBy(() -> boardService.createBoard(request))
                .isInstanceOf(ProfileApplicationException.class);

    }

    @DisplayName("유저의 로그인id를 통해 유저가 작성한 게시글을 조회한다.")
    @Test
    void findByUserId() {
        //given
        User user = createUser("wlsdks12");
        Board board1 = createBoard(user, "테스트1");
        Board board2 = createBoard(user, "테스트2");
        Board board3 = createBoard(user, "테스트3");

        userRepository.save(user);
        List<Board> boards = boardRepository.saveAll(List.of(board1, board2, board3));

        //when
        List<BoardDto> boardList = boardService.findByLoginId(user.getLoginId());

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
        assertThatThrownBy(() -> boardService.findByLoginId(loginId))
                .isInstanceOf(ProfileApplicationException.class);
    }

    @DisplayName("조회하고싶은 게시글을 클릭하면 그 게시글의 id로 게시글 단건을 조회한다.")
    @Test
    void selectBoard() {
        //given
        User user = createUser("wlsdks12");
        Board board = createBoard(user);
        BoardRequest request = BoardRequest.fromEntity(board);
        BoardDto savedBoard = boardService.createBoard(request);

        //when
        BoardDto actual = boardService.selectBoard(savedBoard.boardId());

        //then
        assertThat(actual).isEqualTo(savedBoard);
        assertThat(actual).isInstanceOf(BoardDto.class);

    }

    @DisplayName("작성된 게시글의 pk값인 id를 통해서 게시글의 정보를 조회한다.")
    @Test
    void test() {
        //given
        User user = createUser("wlsdks12");
        User savedUser = userRepository.save(user);
        Board board = createBoard(savedUser, "test");
        Board savedBoard = boardRepository.save(board);

        //when
        BoardDto actual = boardService.findById(savedBoard.getId());

        //then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(BoardDto.fromEntity(savedBoard));
        assertThat(actual).isInstanceOf(BoardDto.class);
    }

    @DisplayName("게시글을 수정하기 전에 게시글을 작성한 유저가 수정하고자하는 유저와 같은지 검증한다.")
    @Test
    void validUser() {
        //given
        User user = createUser("dig04058");
        User savedUser = userRepository.save(user);

        Board board = createBoard(savedUser, "test");
        Board savedBoard = boardRepository.save(board);

        //when
        boolean actual = boardService.validUser(savedBoard.getId(), savedUser.getLoginId());

        //then
        assertThat(actual).isTrue();

    }


    private Board createBoard(User user) {
        return Board.of("테스트 게시글",
                "테스트",
                10,
                20,
                user
        );
    }

    private Board createBoard(User user, String title) {
        return Board.of(title,
                "테스트",
                10,
                20,
                user
        );
    }

    private User createUser(String loginId) {
        return User.of(
                loginId,
                "wlsdks12",
                "wlsdks",
                "wlsdks12@naver.com",
                RoleType.ADMIN,
                UserStatus.Y
        );
    }

}