package com.jinan.profile.dto.board;

import com.jinan.profile.controller.board.request.BoardSubCommentRequest;
import com.jinan.profile.domain.board.BoardSubComment;
import com.jinan.profile.dto.user.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link BoardSubComment}
 */
public record BoardSubCommentDto(
        Long boardSubCommentId,
        String content,
        UserDto userDto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // 생성자 factory method 생성
    public static BoardSubCommentDto of(Long boardSubCommentId, String content, UserDto userDto, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardSubCommentDto(boardSubCommentId, content, userDto, createdAt, updatedAt);
    }

    public static BoardSubCommentDto fromEntity(BoardSubComment entity) {
        return BoardSubCommentDto.of(
                entity.getId(),
                entity.getContent(),
                UserDto.fromEntity(entity.getUser()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // request -> dto 변환 메서드
    public static BoardSubCommentDto fromRequest(BoardSubCommentRequest request) {
        return new BoardSubCommentDto(
                null,
                request.getContent(),
                null,
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }

}