package com.jinan.profile.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinan.profile.config.ControllerTestSupport;
import com.jinan.profile.config.TestSecurityConfig;
import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.service.board.BoardService;
import org.assertj.core.api.Assertions;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[Board] - 컨트롤러 테스트")
class BoardControllerTest extends ControllerTestSupport {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private BoardRepository boardRepository;
    @MockBean private BoardService boardService;

    @DisplayName("메인페이지에서는 누구나 모든 게시글을 볼 수 있다.")
    @Test
    void test() throws Exception {
        //given
        List<BoardDto> boardDtoList = Arrays.asList(
                new BoardDto(1L, "Title 1", "Content 1", 100, 10, LocalDateTime.now(), LocalDateTime.now()),
                new BoardDto(2L, "Title 2", "Content 2", 200, 20, LocalDateTime.now(), LocalDateTime.now())
        );

        when(boardService.selectAllBoardList()).thenReturn(boardDtoList);

        //when & then
        mockMvc.perform(get("/board/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("/board/list")) // return하는 view 검증
                .andExpect(model().attributeExists("boardList")) // "boardList" 속성이 존재하는지 검증
                .andExpect(model().attribute("boardList", Matchers.hasSize(2))) //"boardList" 속성의 크기가 2인지 검증
                .andExpect(model().attribute("boardList", Matchers.hasItem(
                        Matchers.allOf(
                                new FeatureMatcher<BoardDto, Long>(Matchers.is(1L), "boardId", "boardId") {
                                    @Override
                                    protected Long featureValueOf(BoardDto actual) {
                                        return actual.boardId();
                                    }
                                },
                                new FeatureMatcher<BoardDto, String>(Matchers.is("Title 1"), "title", "title") {
                                    @Override
                                    protected String featureValueOf(BoardDto actual) {
                                        return actual.title();
                                    }
                                },
                                new FeatureMatcher<BoardDto, String>(Matchers.is("Content 1"), "content", "content") {
                                    @Override
                                    protected String featureValueOf(BoardDto actual) {
                                        return actual.content();
                                    }
                                },
                                new FeatureMatcher<BoardDto, Integer>(Matchers.is(100), "views", "views") {
                                    @Override
                                    protected Integer featureValueOf(BoardDto actual) {
                                        return actual.views();
                                    }
                                },
                                new FeatureMatcher<BoardDto, Integer>(Matchers.is(10), "likes", "likes") {
                                    @Override
                                    protected Integer featureValueOf(BoardDto actual) {
                                        return actual.likes();
                                    }
                                }
                        )
                ))); // "boardList" 속성에 특정 조건을 만족하는 항목이 포함되어 있는지 검증
    }


    @DisplayName("사용자가 게시글을 작성하고 저장하면 게시글이 저장된다.")
    @Test
    void boardCreate() throws Exception {
        //given
        User user = createUser();
        Board board = createBoard(user, "테스트 데이터");
        BoardRequest request = BoardRequest.of(board);

        //when
        mockMvc.perform(post("/board/createBoard")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("사용자가 게시글을 조회한다.")
    @Test
    void boardSelect() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/board/{boardId}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유저의 로그인id를 통해 그 유저의 모든 게시글을 조회한다.")
    @Test
    void loginIdAllBoardSelect() throws Exception {
        //given
        User user = createUser();
        Board board = createBoard(user, "테스트");

        //when & then
        mockMvc.perform(get("/board/getAllBoard", "wlsdks12"))
                .andDo(print())
                .andExpect(status().isOk());
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