package com.jinan.profile.controller.board.response;

import com.jinan.profile.dto.board.BoardSubCommentDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class BoardSubCommentResponse {

    Long boardSubCommentId;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Builder
    private BoardSubCommentResponse(Long boardSubCommentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.boardSubCommentId = boardSubCommentId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 생성자 메서드
    public static BoardSubCommentResponse of(Long boardSubCommentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardSubCommentResponse(
                boardSubCommentId, content, createdAt, updatedAt
        );
    }

    // dto -> response 변환 메서드
    public static BoardSubCommentResponse fromDto(BoardSubCommentDto dto) {
        return new BoardSubCommentResponse(
                dto.boardSubCommentId(),
                dto.content(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }

}
