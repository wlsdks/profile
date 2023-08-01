package com.jinan.profile.dto.message;

import com.jinan.profile.domain.chat.Message;
import com.jinan.profile.dto.user.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.jinan.profile.domain.chat.Message}
 */
public record MessageDto(
        Long id,
        UserDto user,
        ChatRoomDto chatRoom,
        String text,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // 생성자 factory method 선언
    public static MessageDto of(Long id, UserDto user, ChatRoomDto chatRoom, String text, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new MessageDto(id, user, chatRoom, text, createdAt, updatedAt);
    }

    // entity를 dto로 변환하는 메서드
    public static MessageDto fromEntity(Message entity) {
        return MessageDto.of(
                entity.getId(),
                UserDto.fromEntity(entity.getUser()),
                ChatRoomDto.fromEntity(entity.getChatRoom()),
                entity.getText(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}