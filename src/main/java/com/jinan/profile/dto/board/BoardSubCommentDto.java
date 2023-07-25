package com.jinan.profile.dto.board;

import com.jinan.profile.domain.board.BoardSubComment;

import java.time.LocalDateTime;

/**
 * DTO for {@link BoardSubComment}
 */
public record BoardSubCommentDto(
        Long boardSubCommentId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // 생성자 factory method 생성
    public BoardSubCommentDto of(Long boardSubCommentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardSubCommentDto(boardSubCommentId, content, createdAt, updatedAt);
    }

    public static BoardSubCommentDto fromEntity(BoardSubComment entity) {
        return new BoardSubCommentDto(
                entity.getId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}