package com.jinan.profile.controller.chat;

import com.jinan.profile.domain.user.Users;
import com.jinan.profile.dto.message.MessageDto;
import com.jinan.profile.dto.message.MessageRequestDto;
import com.jinan.profile.service.chat.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final MessageService messageService;

    // 메시지를 가져오는 컨트롤러 메서드
    @PostMapping("/messages")
    public MessageDto saveMessage(@RequestBody MessageRequestDto requestDto) {
        return messageService.saveMessage(requestDto.getUserId(), requestDto.getChatRoomId(), requestDto.getText());
    }

    // 특정 채팅방의 모든 메시지를 가져오는 컨트롤러 메서드
    @GetMapping("/messages/{chatroomId}")
    public List<MessageDto> getMessages(@PathVariable Long chatroomId) {
        return messageService.findByChatRoomId(chatroomId);
    }
}
