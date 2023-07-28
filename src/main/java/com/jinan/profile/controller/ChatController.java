package com.jinan.profile.controller;

import com.jinan.profile.domain.message.ChatRoom;
import com.jinan.profile.dto.request.ChatRequest;
import com.jinan.profile.service.message.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    //    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    /**
     * SimpMessagingTemplate은 @EnableWebSocketMessageBroker를 통해서 등록되는 Bean이다.
     * Broker로 메시지를 전달한다.
     */
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * MessageMapping 어노테이션은 Client가 SEND를 할 수 있는 경로이다.
     * WebSocketConfig에서 등록한 applicationDestinationPrfixes와 @MessageMapping의 경로가 합쳐진다.ex: (/publish/messages)
     */
    @MessageMapping("/messages")
    public void chat(@Valid ChatRequest chatRequest) {
//        chatService.save(chatRequest);
        simpMessagingTemplate.convertAndSend(
                "/subscribe/rooms/" + chatRequest.getRoomId(), chatRequest.getMessage()
        );

    }

    // 방 목록을 가져오는 컨트롤러 메서드
    @GetMapping("/room-list")
    public ResponseEntity<List<ChatRoom>> getRoomList() {
        log.info("연결시도");
        List<ChatRoom> roomList = chatRoomService.getAllRooms();  // 모든 방을 가져옵니다.
        return ResponseEntity.ok(roomList);  // 방 목록을 응답으로 반환합니다.
    }

    /**
     * /create-room 엔드포인트에 대한 접근 권한 설정을 확인해보세요.
     * 현재 설정에서는 .anyRequest().authenticated()로 설정되어 있어서, 인증된 사용자만 모든 요청을 할 수 있습니다.
     * 만약 /create-room 엔드포인트를 인증 없이 접근하려고 했다면 403 Forbidden 오류가 발생할 수 있습니다.
     */
    // 방을 생성하는 컨트롤러 메서드
    @PostMapping("/create-room")
    public ChatRoom createRoom(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        log.info("생성시도");
        return chatRoomService.createRoom(name);
    }

}
