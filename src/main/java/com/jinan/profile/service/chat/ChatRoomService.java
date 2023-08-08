package com.jinan.profile.service.chat;

import com.jinan.profile.domain.chat.ChatRoom;
import com.jinan.profile.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
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
