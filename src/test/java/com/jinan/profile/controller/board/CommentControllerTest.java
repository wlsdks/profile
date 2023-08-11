package com.jinan.profile.controller.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinan.profile.config.ControllerTestSupport;
import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.controller.user.request.UserRequest;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.service.UserService;
import com.jinan.profile.service.board.BoardService;
import com.jinan.profile.service.board.CommentService;
import com.jinan.profile.service.pagination.PaginationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.thymeleaf.spring6.processor.SpringObjectTagProcessor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Comment] - 댓글 컨트롤러 테스트")
class CommentControllerTest extends ControllerTestSupport {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;

    @MockBean private CommentService commentService;
    @MockBean private BoardService boardService;
    @MockBean private UserService userService;
    @MockBean private PaginationService paginationService;


    @DisplayName("사용자가 댓글을 저장하면 댓글이 db에 저장된다.")
    @Test
    void test() throws Exception {
        //given
        BoardCommentRequest boardCommentRequest = createBoardCommentRequest();
        String jsonResult = objectMapper.writeValueAsString(boardCommentRequest);

        given(commentService.createComment(any(BoardCommentRequest.class))).willReturn(any(BoardCommentDto.class));

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