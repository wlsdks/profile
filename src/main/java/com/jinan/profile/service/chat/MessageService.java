package com.jinan.profile.service.chat;

import com.jinan.profile.domain.chat.ChatMap;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.repository.chat.ChatMapRepository;
import com.jinan.profile.domain.chat.ChatRoom;
import com.jinan.profile.domain.chat.Message;
import com.jinan.profile.dto.message.MessageDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.chat.ChatRoomRepository;
import com.jinan.profile.repository.chat.MessageRepository;
import com.jinan.profile.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMapRepository chatMapRepository;

    /**
     * 채팅내용을 저장하고 유저와 채팅방을 매핑해주는 메서드
     */
    public MessageDto saveMessage(Long userId, Long chatRoomId, String text) {
        // 1. user를 찾아온다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));
        // 2. 채팅방을 찾아온다.
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.CHATROOM_NOT_FOUND));

        // 3. 유저-채팅방 매핑을 하고 db에 저장한다.
        chatMapValidateAndSave(user, chatRoom);

        // 4. 메시지를 만들고 db에 저장한다.
        Message savedMessage = messageSave(text, user, chatRoom);
        // 5. 저장한 메시지를 controller에 dto형태로 반환한다.
        return MessageDto.fromEntity(savedMessage);
    }

    /**
     * 특정 채팅방의 모든 메시지를 가져온다.
     */
    public List<MessageDto> findByChatRoomId(Long chatroomId) {
        return messageRepository.findByChatRoomId(chatroomId)
                .stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 유저-채팅방 매핑정보가 있는지 확인하고 없다면 만들어서 저장한다.
     */
    private void chatMapValidateAndSave(User user, ChatRoom chatRoom) {
        chatMapRepository.findByUserAndChatRoom(user, chatRoom)
                .orElseGet(() -> {
                    ChatMap newChatMap = ChatMap.of(user, chatRoom);
                    return chatMapRepository.save(newChatMap);
                });
    }

    /**
     * user, chatRoom, text를 받아서 메시지를 만들고 db에 저장한다.
     */
    private Message messageSave(String text, User user, ChatRoom chatRoom) {
        Message message = Message.of(user, chatRoom, text);
        Message savedMessage = messageRepository.save(message);
        return savedMessage;
    }
}
