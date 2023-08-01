package com.jinan.profile.dto.message;

import com.jinan.profile.domain.chat.ChatMap;
import com.jinan.profile.dto.user.UserDto;

/**
 * DTO for {@link com.jinan.profile.domain.chat.ChatMap}
 */
public record ChatMapDto(
        Long id,
        UserDto user,
        ChatRoomDto chatRoom
) {

    // 생성자 factory method of선언
    public static ChatMapDto of(Long id, UserDto user, ChatRoomDto chatRoom) {
        return new ChatMapDto(id, user, chatRoom);
    }

    // entity를 dto로 변환하는 factory method
    public static ChatMapDto fromEntity(ChatMap entity) {
        return ChatMapDto.of(
                entity.getId(),
                UserDto.fromEntity(entity.getUser()),
                ChatRoomDto.fromEntity(entity.getChatRoom())
        );
    }
}