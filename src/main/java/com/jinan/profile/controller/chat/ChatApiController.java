package com.jinan.profile.controller.chat;

import com.jinan.profile.domain.chat.ChatRoom;
import com.jinan.profile.dto.message.ChatRequest;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.service.UserService;
import com.jinan.profile.service.chat.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatApiController {

    /**
     * SimpMessagingTemplate은 @EnableWebSocketMessageBroker를 통해서 등록되는 Bean이다. Broker로 메시지를 전달한다.
     */
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRoomService chatRoomService;
    private final UserService userService;


    /**
     * MessageMapping 어노테이션은 Client가 SEND를 할 수 있는 경로이다.
     * WebSocketConfig에서 등록한 applicationDestinationPrfixes와 @MessageMapping의 경로가 합쳐진다.ex: (/publish/messages)
     */
    @MessageMapping("/messages")
    public void chat(@Valid ChatRequest chatRequest, Authentication authentication) {
        chatRequest.setUserName(authentication.getName());
        simpMessagingTemplate.convertAndSend(
                "/subscribe/rooms/" + chatRequest.getRoomId(), chatRequest
        );
    }


    /**
     * 채팅방 목록을 가져오는 컨트롤러
     */
    @GetMapping("/room-list")
    public ResponseEntity<List<ChatRoom>> getRoomList() {
        List<ChatRoom> roomList = chatRoomService.getAllRooms();  // 모든 방을 가져옵니다.
        return ResponseEntity.ok(roomList);  // 방 목록을 응답으로 반환합니다.
    }

    /**
     * 채팅방을 생성하는 컨트롤러
     */
    @PostMapping("/create-room")
    public ChatRoom createRoom(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        return chatRoomService.createRoom(name);
    }

    /**
     * 채팅방에서 지금 채팅보내는 유저정보를 받아오는 컨트롤러 시큐리티로부터 정보를 받아온다.
     */
    @GetMapping("/current-user")
    public UserDto currentUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByLoginId(username);
    }

}
