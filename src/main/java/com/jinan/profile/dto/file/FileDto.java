package com.jinan.profile.dto.file;

import com.jinan.profile.domain.file.File;
import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.dto.user.UsersDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.jinan.profile.domain.file.File}
 */
public record FileDto(
        Long id,
        UsersDto users,
        BoardDto board,
        String fileName,
        String fileUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // 생성자 factory method of선언
    public static FileDto of(Long id, UsersDto users, BoardDto board, String fileName, String fileUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new FileDto(id, users, board, fileName, fileUrl, createdAt, updatedAt);
    }

    // entity를 받아서 dto로 만드는 메서드 선언
    public static FileDto fromEntity(File entity) {
        return FileDto.of(
                entity.getId(),
                UsersDto.fromEntity(entity.getUsers()),
                BoardDto.fromEntity(entity.getBoard()),
                entity.getFileName(),
                entity.getFileUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}