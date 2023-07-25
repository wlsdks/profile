package com.jinan.profile.dto.message;

import com.jinan.profile.domain.message.ChatMap;
import com.jinan.profile.domain.user.Users;
import com.jinan.profile.dto.user.UsersDto;

import java.io.Serializable;

/**
 * DTO for {@link com.jinan.profile.domain.message.ChatMap}
 */
public record ChatMapDto(
        Long id,
        UsersDto users,
        ChatRoomDto chatRoom
) {

    // 생성자 factory method of선언
    public static ChatMapDto of(Long id, UsersDto users, ChatRoomDto chatRoom) {
        return new ChatMapDto(id, users, chatRoom);
    }

    // entity를 dto로 변환하는 factory method
    public static ChatMapDto fromEntity(ChatMap entity) {
        return ChatMapDto.of(
                entity.getId(),
                UsersDto.fromEntity(entity.getUsers()),
                ChatRoomDto.fromEntity(entity.getChatRoom())
        );
    }
}