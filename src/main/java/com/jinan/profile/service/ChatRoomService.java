package com.jinan.profile.service;

import com.jinan.profile.domain.message.ChatRoom;
import com.jinan.profile.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    // 채팅방을 생성한다.
    public ChatRoom createRoom(String username) {
        ChatRoom chatRoom = ChatRoom.of(username);
        return chatRoomRepository.save(chatRoom);
    }

    // 모든 채팅방을 가져온다.
    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

}