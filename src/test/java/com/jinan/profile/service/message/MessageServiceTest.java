package com.jinan.profile.service.message;

import com.jinan.profile.config.TestSecurityConfig;
import com.jinan.profile.domain.message.ChatRoom;
import com.jinan.profile.domain.type.RoleType;
import com.jinan.profile.domain.user.Users;
import com.jinan.profile.dto.message.MessageDto;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.ChatRoomRepository;
import com.jinan.profile.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@Profile("test")
@Import(TestSecurityConfig.class)
@DisplayName("실시간 채팅 서비스레이어 테스트")
@SpringBootTest
class MessageServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageService messageService;

    @Transactional
    @DisplayName("사용자가 채팅방에서 메시지를 입력해서 보내면 db에 저장된다.")
    @Test
    void saveMessage() {
        //given
        Users testUser = Users.of("jinan", "wlsdks12@naver.com", "wlsdks12", RoleType.ADMIN);
        ChatRoom testChatRoom = ChatRoom.of("jinanChatRoom");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(testChatRoom));

        //when
        MessageDto actual = messageService.saveMessage(1L, 1L, "test message");

        //then
        assertThat(actual).isInstanceOf(MessageDto.class);
    }

    @DisplayName("존재하지않는 사용자가 메시지를 입력해서 보내면 예외가 발생한다.")
    @Test
    void saveMessageException() {
        //given
        Users testUser = Users.of("jinan", "wlsdks12@naver.com", "wlsdks12", RoleType.ADMIN);
        ChatRoom testChatRoom = ChatRoom.of("jinanChatRoom");

        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(testChatRoom));

        //when&then
        assertThatThrownBy(() -> messageService.saveMessage(1L, 1L, "test message"))
                .isInstanceOf(ProfileApplicationException.class);
//                .hasMessage("유저를 찾을수 없습니다.");

    }

}