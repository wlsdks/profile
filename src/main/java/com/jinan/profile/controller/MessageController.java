package com.jinan.profile.controller;

import com.jinan.profile.domain.message.Message;
import com.jinan.profile.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final MessageService messageService;

    // 메시지를 가져오는 컨트롤러 메서드
    @PostMapping("/messages")
    public Message saveMessage(@RequestBody Message message) {
        return messageService.saveMessage(message);
    }

    // 특정 채팅방의 모든 메시지를 가져오는 컨트롤러 메서드
    @GetMapping("/messages/{chatroomId}")
    public List<Message> getMessagesByChatroomId(@PathVariable Long chatroomId) {
        return messageService.getMessageByChatroomId(chatroomId);
    }

}
