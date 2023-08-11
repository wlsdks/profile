package com.jinan.profile.config;

import com.jinan.profile.controller.board.BoardController;
import com.jinan.profile.controller.board.CommentController;
import com.jinan.profile.controller.chat.ChatMainController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Import(TestSecurityConfig.class) // 테스트 설정 클래스 적용
@WebMvcTest({
        ChatMainController.class,
        BoardController.class,
        CommentController.class
})
public abstract class ControllerTestSupport {

}
