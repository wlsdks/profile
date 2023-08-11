package com.jinan.profile.controller.board.request;

import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.domain.board.BoardSubComment;
import com.jinan.profile.dto.board.BoardSubCommentDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class BoardSubCommentRequest {

    Long boardSubCommentId;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Builder
    private BoardSubCommentRequest(Long boardSubCommentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.boardSubCommentId = boardSubCommentId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BoardSubCommentRequest of(Long boardSubCommentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardSubCommentRequest(
                boardSubCommentId, content, createdAt, updatedAt
        );
    }

    // dto -> request
    public static BoardSubCommentRequest fromDto(BoardSubCommentDto dto) {
        return of(
                dto.boardSubCommentId(),
                dto.content(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }


}
