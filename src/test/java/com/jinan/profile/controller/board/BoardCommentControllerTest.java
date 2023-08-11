package com.jinan.profile.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinan.profile.config.ControllerTestSupport;
import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.service.UserService;
import com.jinan.profile.service.board.BoardCommentService;
import com.jinan.profile.service.board.BoardService;
import com.jinan.profile.service.pagination.PaginationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Comment] - 댓글 컨트롤러 테스트")
class BoardCommentControllerTest extends ControllerTestSupport {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;

    @MockBean private BoardCommentService boardCommentService;
    @MockBean private BoardService boardService;
    @MockBean private UserService userService;
    @MockBean private PaginationService paginationService;

    @DisplayName("게시글에 들어갔을때 그 게시글이 가진 댓글을 모두 가져온다.")
    @Test
    public void selectBoardComment() throws Exception {
        //given
        List<BoardCommentDto> mockList = new ArrayList<>();
        given(boardCommentService.getBoardComment(anyLong())).willReturn(mockList);

        //when & then
        mockMvc.perform(get("/board/comment/get/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")); // 빈 배열을 기대
    }

    @DisplayName("사용자가 댓글을 저장하면 댓글이 db에 저장된다.")
    @Test
    void createComment() throws Exception {
        //given
        BoardCommentRequest boardCommentRequest = createBoardCommentRequest();
        String jsonResult = objectMapper.writeValueAsString(boardCommentRequest);

        given(boardCommentService.createComment(any(BoardCommentRequest.class), anyLong())).willReturn(any(BoardCommentDto.class));

        //when & then
        mockMvc.perform(post("/board/comment/create") // POST 메서드로 변경
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult))
                .andExpect(status().isOk());
    }


    private static BoardCommentRequest createBoardCommentRequest() {
        return BoardCommentRequest.builder()
                .boardCommentId(1L)
                .content("test")
                .build();
    }

}