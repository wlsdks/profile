package com.jinan.profile.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinan.profile.config.ControllerTestSupport;
import com.jinan.profile.config.TestSecurityConfig;
import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.service.board.BoardService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@DisplayName("[Board] - 컨트롤러 테스트")
class BoardControllerTest extends ControllerTestSupport {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private BoardRepository boardRepository;
    @MockBean private BoardService boardService;

    @DisplayName("사용자가 게시글을 작성하고 저장하면 게시글이 저장된다.")
    @Test
    void boardCreate() throws Exception {
        //given
        User user = createUser();
        Board board = createBoard(user, "테스트 데이터");
        BoardRequest request = BoardRequest.of(board);

        //when
        mockMvc.perform(post("/board/save")
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