package com.jinan.profile.service.message;

import com.jinan.profile.domain.message.ChatRoom;
import com.jinan.profile.domain.message.Message;
import com.jinan.profile.domain.user.Users;
import com.jinan.profile.dto.message.MessageDto;
import com.jinan.profile.repository.ChatRoomRepository;
import com.jinan.profile.repository.MessageRepository;
import com.jinan.profile.repository.UserRepository;
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

    // 메시지를 저장한다.
    public MessageDto saveMessage(Long userId, Long chatRoomId, String text) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 UserId: " + userId));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 chatRoomId: " + chatRoomId));

        // message를 만들어준다.
        Message message = Message.of(user, chatRoom, text);
        Message savedMessage = messageRepository.save(message);
        return MessageDto.fromEntity(savedMessage);  // MessageDto는 Message 엔티티를 DTO로 변환하는 메소드를 가지고 있어야 합니다.
    }


    // 특정 채팅방의 모든 메시지를 가져온다ㅋ
    public List<MessageDto> getMessageByChatroomId(Long chatroomId) {
        return messageRepository.findByChatRoomId(chatroomId)
                .stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }
}
