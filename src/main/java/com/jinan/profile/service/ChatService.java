package com.jinan.profile.service;

import com.jinan.profile.dto.request.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final SimpMessageSendingOperations template;

    /**
     * MessageMapping 어노테이션은 pub으로 요청이 왔을때 해당 요청을 받아 처리하게 된다.
     * ChatRequest는 직접 만들어준 request 객체이다.
     */
    @MessageMapping("/chat/enter")
    public void enter(@Payload ChatRequest chatRequest, SimpMessageSendingOperations headerAccesor) {
        template.convertAndSend("/sub/chat/room/" + chatRequest.getRoomId(), chatRequest);
    }
}
