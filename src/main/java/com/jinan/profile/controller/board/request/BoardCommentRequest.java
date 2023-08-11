package com.jinan.profile.controller.board.request;

import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.dto.board.BoardSubCommentDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class BoardCommentRequest {

    Long boardCommentId;
    String content;
    List<BoardSubCommentRequest> boardSubComments;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Builder
    private BoardCommentRequest(Long boardCommentId, String content, List<BoardSubCommentRequest> boardSubComments, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.boardCommentId = boardCommentId;
        this.content = content;
        this.boardSubComments = boardSubComments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // dto -> request factory method 선언
    public static BoardCommentRequest fromDto(BoardCommentDto dto) {
        return new BoardCommentRequest(
                dto.boardCommentId(),
                dto.content(),
                dto.boardSubComments().stream()
                        .map(BoardSubCommentRequest::fromDto)
                        .toList(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }


}
