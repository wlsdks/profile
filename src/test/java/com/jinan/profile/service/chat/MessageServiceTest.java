package com.jinan.profile.service.chat;

import com.jinan.profile.config.TestSecurityConfig;
import com.jinan.profile.domain.chat.ChatMap;
import com.jinan.profile.domain.chat.ChatRoom;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.message.ChatRoomDto;
import com.jinan.profile.dto.message.MessageDto;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.chat.ChatMapRepository;
import com.jinan.profile.repository.chat.ChatRoomRepository;
import com.jinan.profile.repository.user.UserRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@DisplayName("실시간 채팅 서비스레이어 테스트")
@Profile("test")
@Transactional(readOnly = true)
@Import(TestSecurityConfig.class)
@SpringBootTest
class MessageServiceTest {

    @MockBean private UserRepository userRepository;
    @MockBean private ChatRoomRepository chatRoomRepository;
    @MockBean private ChatMapRepository chatMapRepository;
    @Autowired private MessageService messageService;


    @DisplayName("사용자가 채팅방에서 메시지를 입력해서 보내면 db에 저장된다.")
    @Test
    void saveMessage() {
        //given
        User testUser = User.of("wlsdks12", "wlsdks12", "wlsdks12", "wlsdks12@naver.com", RoleType.ADMIN, UserStatus.D);

        ChatRoom testChatRoom = ChatRoom.of("jinanChatRoom");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(testChatRoom));

        //when
        MessageDto actual = messageService.saveMessage(1L, 1L, "test message");

        //then
        assertThat(actual).isNotNull();
        assertThat(actual).isInstanceOf(MessageDto.class);
        assertThat(actual.text()).isEqualTo("test message");
        assertThat(actual.user()).isEqualTo(UserDto.fromEntity(testUser));
    }

    @DisplayName("존재하지않는 사용자가 메시지를 입력해서 보내면 예외가 발생한다.")
    @Test
    void saveMessageException() {
        //given
        User testUser = User.of("jinan", "wlsdks12", "wlsdks12", "wlsdks12@naver.com", RoleType.ADMIN, UserStatus.D);
        ChatRoom testChatRoom = ChatRoom.of("jinanChatRoom");

        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(testChatRoom));

        //when&then
        assertThatThrownBy(() -> messageService.saveMessage(1L, 1L, "test message"))
                .isInstanceOf(ProfileApplicationException.class);
//                .hasMessage("유저를 찾을수 없습니다.");

    }

    @DisplayName("사용자와 채팅방을 연결해주는 map에 데이터가 들어가야 한다.")
    @Test
    void test() {
        //given
        User testUser = User.of("jinan", "wlsdks12", "wlsdks12", "wlsdks12@naver.com", RoleType.ADMIN, UserStatus.D);

        ChatRoom testChatRoom = ChatRoom.of("jinanChatRoom");
        ChatMap testChatMap = ChatMap.of(testUser, testChatRoom);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(testChatRoom));
        when(chatMapRepository.save(any(ChatMap.class))).thenReturn(testChatMap);

        //when
        MessageDto actual = messageService.saveMessage(1L, 1L, "test message");

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.text()).isEqualTo("test message");
        assertThat(actual.user()).isEqualTo(UserDto.fromEntity(testUser));
        assertThat(actual.chatRoom()).isEqualTo(ChatRoomDto.fromEntity(testChatRoom));

    }

}