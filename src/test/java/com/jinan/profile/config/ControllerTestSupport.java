package com.jinan.profile.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinan.profile.controller.board.BoardCommentController;
import com.jinan.profile.controller.board.BoardController;
import com.jinan.profile.controller.board.BoardSubCommentController;
import com.jinan.profile.controller.chat.ChatMainController;
import com.jinan.profile.repository.board.BoardCommentRepository;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.repository.user.UserRepository;
import com.jinan.profile.service.UserService;
import com.jinan.profile.service.board.BoardCommentService;
import com.jinan.profile.service.board.BoardService;
import com.jinan.profile.service.board.BoardSubCommentService;
import com.jinan.profile.service.pagination.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class) // 테스트 설정 클래스 적용
@WebMvcTest({
        ChatMainController.class,
        BoardController.class,
        BoardCommentController.class,
        BoardSubCommentController.class
})
public abstract class ControllerTestSupport {

    // util
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;

    // mock service
    @MockBean protected UserService userService;
    @MockBean protected BoardService boardService;
    @MockBean protected PaginationService paginationService;
    @MockBean protected BoardCommentService boardCommentService;
    @MockBean protected BoardSubCommentService boardSubCommentService;

    // mock repository
    @MockBean protected BoardCommentRepository boardCommentRepository;
    @MockBean protected UserRepository userRepository;
    @MockBean protected BoardRepository boardRepository;

}
