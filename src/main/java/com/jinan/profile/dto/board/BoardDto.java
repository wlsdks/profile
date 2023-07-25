package com.jinan.profile.dto.board;

import com.jinan.profile.domain.board.Board;

import java.time.LocalDateTime;

/**
 * DTO for {@link Board}
 */
public record BoardDto(
        Long boardId,
        String title,
        String content,
        int views,
        int likes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method of 선언
    public BoardDto of(Long boardId, String title, String content, int views, int likes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardDto(boardId, title, content, views, likes, createdAt, updatedAt);
    }

    // 서비스 레이어에서 entity를 dto로 변환시켜주는 코드
    public static BoardDto fromEntity(Board entity) {
        return new BoardDto(
                entity.getBoardId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getViews(),
                entity.getLikes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}