package com.jinan.profile.service.message;

import com.jinan.profile.domain.message.Message;
import com.jinan.profile.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    // 메시지를 저장한다.
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    // 특정 채팅방의 모든 메시지를 가져온다.
    public List<Message> getMessageByChatroomId(Long chatroomId) {
        return messageRepository.findByChatroomId(chatroomId);
    }
}
