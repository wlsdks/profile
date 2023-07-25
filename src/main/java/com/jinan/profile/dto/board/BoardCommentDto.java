package com.jinan.profile.dto.board;

import com.jinan.profile.domain.board.BoardComment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link BoardComment}
 */
public record BoardCommentDto(
        Long boardCommentId,
        String content,
        List<BoardSubCommentDto> boardSubComments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method 선언
    public static BoardCommentDto of(Long boardCommentId, String content, List<BoardSubCommentDto> boardSubComments, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardCommentDto(boardCommentId, content, boardSubComments, createdAt, updatedAt);
    }

    // entity를 받아서 dto를 만들어주는 코드
    public static BoardCommentDto fromEntity(BoardComment entity) {
        return new BoardCommentDto(
                entity.getBoardCommentId(),
                entity.getContent(),
                entity.getBoardSubComments().stream()
                        .map(BoardSubCommentDto::fromEntity)
                        .collect(Collectors.toList()), // 순환참조가 안되도록 잘 확인하자
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}