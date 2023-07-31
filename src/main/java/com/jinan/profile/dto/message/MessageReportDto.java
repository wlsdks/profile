package com.jinan.profile.dto.message;

import com.jinan.profile.domain.chat.MessageReport;
import com.jinan.profile.dto.user.UsersDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.jinan.profile.domain.chat.MessageReport}
 */
public record MessageReportDto(
        Long id,
        UsersDto messageReporter,
        MessageDto message,
        String reason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method 선언
    public static MessageReportDto of(Long id, UsersDto messageReporter, MessageDto message, String reason, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new MessageReportDto(id, messageReporter, message, reason, createdAt, updatedAt);
    }

    //entity를 받아서 dto를 생성하는 factory method선언
    public static MessageReportDto fromEntity(MessageReport entity) {
        return MessageReportDto.of(
                entity.getId(),
                UsersDto.fromEntity(entity.getMessageReporter()),
                MessageDto.fromEntity(entity.getMessage()),
                entity.getReason(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}