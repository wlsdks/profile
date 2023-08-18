package com.jinan.profile.controller.board;

import com.jinan.profile.config.ControllerTestSupport;
import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.dto.board.BoardCommentDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("댓글 controller 테스트")
class BoardCommentControllerTest extends ControllerTestSupport {

    @DisplayName("게시글에 들어갔을때 그 게시글이 가진 댓글을 모두 가져온다.")
    @Test
    public void selectBoardComment() throws Exception {
        //given
        Page<BoardCommentDto> mockList = new PageImpl<>(Collections.emptyList());
        given(boardCommentService.getBoardComment(anyLong(), any(Pageable.class))).willReturn(mockList);

        //when & then
        mockMvc.perform(get("/board/comment/get/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "wlsdks12")
    @DisplayName("사용자가 댓글을 저장하면 댓글이 db에 저장된다.")
    @Test
    void createComment() throws Exception {
        //given
        BoardCommentRequest boardCommentRequest = createBoardCommentRequest();
        String jsonResult = objectMapper.writeValueAsString(boardCommentRequest);

        //when & then
        mockMvc.perform(post("/board/comment/create/{boardId}", 1L) // POST 메서드로 변경
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult))
                .andExpect(status().isOk());
    }


    private static BoardCommentRequest createBoardCommentRequest() {
        return BoardCommentRequest.builder()
                .content("test")
                .build();
    }

}