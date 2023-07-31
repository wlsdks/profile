package com.jinan.profile.dto.message;

import com.jinan.profile.domain.chat.ChatRoom;

/**
 * DTO for {@link com.jinan.profile.domain.chat.ChatRoom}
 */
public record ChatRoomDto(
        Long id,
        String chatRoomName
) {

    // factory method of 선언
    public static ChatRoomDto of(Long id, String chatRoomName) {
        return new ChatRoomDto(id, chatRoomName);
    }

    // entity를 dto로 변환하는 factory 메서드
    public static ChatRoomDto fromEntity(ChatRoom entity) {
        return ChatRoomDto.of(
                entity.getId(),
                entity.getChatRoomName()
        );
    }
}