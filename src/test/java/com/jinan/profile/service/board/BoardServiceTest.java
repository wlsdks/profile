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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoardServiceTest extends TotalTestSupport {

    @Autowired private BoardService boardService;
    @Autowired private BoardRepository boardRepository;
    @Autowired private UserRepository userRepository;

    @DisplayName("[happy]-등록된 모든 게시글을 조회한다.")
    @Test
    void selectAllBoardList() {
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

    @DisplayName("[happy]-유저가 작성한 게시글을 저장한다.")
    @Test
    void createBoard() {
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
    }

    @DisplayName("[bad]-게시글 제목이 공백인 채로(null말고 empty) 유저가 게시글을 작성하고 저장하면 실패한다.")
    @Test
    void createBoardExceptionWithEmptyTitle() {
        //given
        User user = createUser("wlsdks12");
        Board board = createBoardTitleEmpty(user);
        BoardRequest request = BoardRequest.fromEntity(board);

        //when
        assertThatThrownBy(() -> boardService.createBoard(request))
                .isInstanceOf(ProfileApplicationException.class);
    }

    @DisplayName("[bad]-게시글 내용이 공백인 채로(null말고 empty) 유저가 게시글을 작성하고 저장하면 실패한다.")
    @Test
    void createBoardExceptionWithEmptyContent() {
        //given
        User user = createUser("wlsdks12");
        Board board = createBoardContentEmpty(user);
        BoardRequest request = BoardRequest.fromEntity(board);

        //when
        assertThatThrownBy(() -> boardService.createBoard(request))
                .isInstanceOf(ProfileApplicationException.class);
    }

    @DisplayName("[bad]-게시글 엔티티 안에 유저가 없는(존재해서는 안되는)게시글을 저장하면 예외가 발생한다.")
    @Test
    void createBoardUserNotFoundUserException() {
        //given
        User user = createUser("wlsdks12");
        Board board = createBoard(null);
        BoardRequest request = BoardRequest.fromEntity(board);

        //when & then
        assertThatThrownBy(() -> boardService.createBoard(request))
                .isInstanceOf(ProfileApplicationException.class);

    }

    @DisplayName("[happy]-유저의 로그인id를 통해 유저가 작성한 게시글을 조회한다.")
    @Test
    void findByLoginId() {
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

    @DisplayName("[bad]-존재하지않는 유저의 로그인id로 게시글을 조회하면 예외가 발생한다.")
    @Test
    void findByLoginIdException() {
        //given
        String loginId = "anonymous";

        //when & then
        assertThatThrownBy(() -> boardService.findByLoginId(loginId))
                .isInstanceOf(ProfileApplicationException.class);
    }

    @DisplayName("[happy]-조회하고싶은 게시글을 클릭하면 그 게시글의 id로 게시글 단건을 조회한다.")
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

    @DisplayName("[happy]-작성된 게시글의 pk값인 id를 통해서 게시글의 정보를 조회한다.")
    @Test
    void findById() {
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

    @DisplayName("[bad]-존재하지 않는 게시글pk값으로 게시글의 정보를 조회하면 조회에 실패한다.")
    @Test
    void findByIdException() {
        //given
        User user = createUser("wlsdks12");
        User savedUser = userRepository.save(user);
        Board board = createBoard(savedUser, "test");
        Board savedBoard = boardRepository.save(board);

        //when & then
        assertThatThrownBy(() -> boardService.findById(100L))
                .isInstanceOf(ProfileApplicationException.class);
    }

    @DisplayName("[happy]-게시글을 수정하기 전에 게시글을 작성한 유저가 수정하고자하는 유저와 같은지 검증한다.")
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

    @DisplayName("[bad]-게시글을 작성한 유저가 수정하고자하는 유저와 다르다면 검증은 실패한다.")
    @Test
    void validUserException() {
        //given
        User user = createUser("dig04058");
        User savedUser = userRepository.save(user);

        Board board = createBoard(savedUser, "test");
        Board savedBoard = boardRepository.save(board);

        //when
        boolean validUser = boardService.validUser(savedBoard.getId(), "annonymousId");

        //then
        assertThat(validUser).isFalse();
    }

    @DisplayName("[happy]-게시글을 올린 유저 본인이 게시글을 수정하면 게시글이 수정된다.")
    @Test
    void updateBoard() {
        //given
        User user = createUser("wlsdks12");
        User savedUser = userRepository.save(user);
        Board board = createBoard(user, "test");
        Board savedBoard = boardRepository.save(board);

        BoardRequest boardRequest = BoardRequest.fromEntity(savedBoard);

        //when
        BoardDto boardDto = boardService.updateBoard(boardRequest, savedBoard.getId(), savedUser.getLoginId());

        //then
        assertThat(boardDto).isNotNull();
        assertThat(boardDto).isInstanceOf(BoardDto.class);
        assertThat(boardDto).hasFieldOrPropertyWithValue("title", savedBoard.getTitle());
        assertThat(boardDto).hasFieldOrPropertyWithValue("content", savedBoard.getContent());
    }




    private Board createBoard(User user) {
        return Board.of(
                "테스트 게시글",
                "테스트",
                10,
                20,
                user
        );
    }

    private Board createBoardTitleEmpty(User user) {
        return Board.of(
                "",
                "테스트",
                10,
                20,
                user
        );
    }

    private Board createBoardContentEmpty(User user) {
        return Board.of(
                "테스트 게시글",
                "",
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