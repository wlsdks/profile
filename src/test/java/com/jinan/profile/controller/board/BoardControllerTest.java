package com.jinan.profile.controller.board;

import com.jinan.profile.config.ControllerTestSupport;
import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.dto.user.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("게시글 controller 테스트")
class BoardControllerTest extends ControllerTestSupport {

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

        given(userRepository.findUserByLoginId(anyString())).willReturn(Optional.of(user));
        given(boardRepository.save(any(Board.class))).willReturn(board);

        BoardRequest request = createBoardRequest(board);

        //  테스트 환경에서 이 Authentication 객체를 요청에 주입하기 위해서는 특별한 방법을 사용해야 한다.
        String username = "wlsdks12";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "wlsdks12");

        //when
        mockMvc.perform(post("/board/createBoard")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authentication))) // 이 부분을 사용하여 Authentication 객체를 요청에 주입한다.
                .andDo(print())
                .andExpect(status().isOk());
    }

    private BoardRequest createBoardRequest(Board board) {
        return BoardRequest.builder()
                .id(10L)
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }

    @DisplayName("사용자가 게시글 목록에서 제목을 클릭해서 게시글을 조회한다.")
    @Test
    void boardSelect() throws Exception {
        //given
        User user = createUser();
        BoardDto mockBoardDto = BoardDto.fromEntity(createBoard(user, "board")); // 적절한 값을 설정하세요.
        when(boardService.selectBoard(anyLong())).thenReturn(mockBoardDto);

        //when & then
        mockMvc.perform(get("/board/{boardId}", 1L))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board")) // board 객체가 모델에 있는지 확인
                .andExpect(model().attributeExists("boardId")) // board 객체가 모델에 있는지 확인
                .andExpect(view().name("/board/detail"))
                .andReturn();
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

    @DisplayName("게시글id를 통해 해당하는 게시글의 기존 정보를 모두 가져와서 수정하기 페이지에 뿌려준다.")
    @Test
    void updateViewReturn() throws Exception {
        User user = createUser();
        //given
        Board board = Board.builder()
                .id(1L)
                .title("test")
                .content("test")
                .views(10)
                .user(user)
                .likes(20)
                .build();

        BoardDto boardDto = BoardDto.fromEntity(board);

        given(boardService.findById(anyLong())).willReturn(boardDto);

        //when
        mockMvc.perform(get("/board/update/{boardId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/board/update"))
                .andExpect(model().attributeExists("boardResponse"));

        //then

    }


    /**
     * 테스트 코드 내부의 시큐리티 관련 - authentication 주입방법 설명
     * <p>
     *      .with() 내부의 authentication(authentication) 메서드는 Spring Security의 테스트 지원 클래스인 SecurityMockMvcRequestPostProcessors에서 제공하는 메서드로,
     *      RequestPostProcessor를 반환한다. 이 RequestPostProcessor는 요청이 실행되기 전에 요청에 대한 추가 설정을 수행하는 역할을 한다.
     *      여기서는 요청(request)에 Authentication 객체를 주입하는 작업을 수행하게 된다.
     *      요약하면, .with() 메서드를 사용하여 요청에 Authentication 객체를 주입하는 것은 Spring Security의 특별한 테스트 지원 기능을 사용하는 것이며, 일반적인 요청 파라미터로 Authentication 객체를 전달하는 것이 아니다.
     * </p>
     */
    @DisplayName("게시글을 수정할때는 이 메서드에 요청을 보내서 게시글을 작성한 사람인지 검증한다.")
    @Test
    void validUserController() throws Exception {
        String username = "user";
        //  테스트 환경에서 이 Authentication 객체를 요청에 주입하기 위해서는 특별한 방법을 사용해야 한다.
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "password");

        given(boardService.validUser(anyLong(), eq(username))).willReturn(true);

        mockMvc.perform(
                        get("/board/update/validUser")
                                .param("boardId", "1")
                                // .with() 메서드는 요청 자체에 대한 다양한 설정을 수행할 수 있게 해주는 메서드다. 여기서는 Authentication 객체를 요청에 주입하기 위해 .with(authentication(authentication))를 사용하고 있다.
                                .with(authentication(authentication))) // 이 부분을 사용하여 Authentication 객체를 요청에 주입한다.
                .andExpect(status().isOk());

    }

    @WithMockUser
    @DisplayName("게시글을 작성한 사용자가 수정하기를 요청하면 문제없이 게시글이 수정된다.")
    @Test
    void updateBoardController() throws Exception {
        //given
        BoardRequest mockRequest = mock(BoardRequest.class);
        BoardDto mockBoardDto = mock(BoardDto.class);
        Long boardId = 1L;

        // when & then
        given(boardService.updateBoard(any(BoardRequest.class), eq(boardId), anyString()))
                .willReturn(mockBoardDto);

        mockMvc.perform(post("/board/updateBoard/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().is3xxRedirection())
                .andExpect(status().isFound());
    }


    private Board createBoard(User user, String title) {
        return Board.builder()
                .title(title)
                .content("test")
                .views(10)
                .likes(10)
                .user(user)
                .build();
    }

    private User createUser() {
        return User.builder()
                .loginId("wlsdks12")
                .password("wlsdks12")
                .username("wlsdks12")
                .roleType(RoleType.ADMIN)
                .userStatus(UserStatus.Y)
                .build();
    }

}