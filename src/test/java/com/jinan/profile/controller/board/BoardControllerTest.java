package com.jinan.profile.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinan.profile.config.ControllerTestSupport;
import com.jinan.profile.config.TestSecurityConfig;
import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.controller.board.response.BoardResponse;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.repository.user.UserRepository;
import com.jinan.profile.service.UserService;
import com.jinan.profile.service.board.BoardService;
import com.jinan.profile.service.pagination.PaginationService;
import org.assertj.core.api.Assertions;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[Board] - 컨트롤러 테스트")
class BoardControllerTest extends ControllerTestSupport {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private UserService userService;
    @MockBean private BoardService boardService;
    @MockBean private PaginationService paginationService;


    @Test
    @DisplayName("[페이징 적용] - 메인리스트에는 페이징 처리된 모든 게시글이 표시된다.")
    void testGetList() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        List<BoardDto> boardDtoList = new ArrayList<>(); // 적절한 게시글 DTO 목록을 생성하세요
        Page<BoardDto> boardDtoPage = new PageImpl<>(boardDtoList, pageable, boardDtoList.size());
        List<Integer> barNumbers = Arrays.asList(1, 2, 3); // 적절한 페이지네이션 바 숫자를 생성하세요

        when(boardService.selectAllBoardList(pageable)).thenReturn(boardDtoPage);
        when(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).thenReturn(barNumbers);

        //when & then
        mockMvc.perform(get("/board/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("/board/list"))
                .andExpect(model().attributeExists("paginationBarNumbers", "boardList", "totalPages", "currentPage"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers))
                .andExpect(model().attribute("boardList", boardDtoPage.getContent()))
                .andExpect(model().attribute("totalPages", boardDtoPage.getTotalPages()))
                .andExpect(model().attribute("currentPage", pageable.getPageNumber()));
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
        User user = createUser();
        BoardDto mockBoardDto = BoardDto.fromEntity(createBoard(user, "board")); // 적절한 값을 설정하세요.
        when(boardService.selectBoard(1L)).thenReturn(mockBoardDto);

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