package com.jinan.profile.dto;

import com.jinan.profile.domain.BoardSubComment;
import com.jinan.profile.domain.Users;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link BoardSubComment}
 */
public record BoardWithCommentsDto(
        Long id,
        UsersDto users,
        List<BoardCommentDto> boardCommentDtos,
        Long boardSubCommentId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // 생성자 factory method 생성
    public BoardWithCommentsDto of(Long boardSubCommentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardWithCommentsDto(boardSubCommentId, content, createdAt, updatedAt);
    }

    public static BoardWithCommentsDto fromEntity(BoardSubComment entity) {
        return new BoardWithCommentsDto(
                entity.getBoardSubCommentId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}