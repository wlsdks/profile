package com.jinan.profile.dto;

import com.jinan.profile.domain.BoardComment;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.jinan.profile.domain.BoardComment}
 */
public record BoardCommentDto(
        Long boardCommentId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method 선언
    public BoardCommentDto of(Long boardCommentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardCommentDto(boardCommentId, content, createdAt, updatedAt);
    }

    // entity를 받아서 dto를 만들어주는 코드
    public static BoardCommentDto fromEntity(BoardComment entity) {
        return new BoardCommentDto(
                entity.getBoardCommentId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}