package com.jinan.profile.controller.board.response;

import com.jinan.profile.controller.user.response.UserResponse;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.dto.board.BoardSubCommentDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class BoardCommentResponse {

    Long boardCommentId;
    String content;
    BoardResponse boardResponse;
    UserResponse userResponse;
    List<BoardSubCommentResponse> boardSubComments;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Builder
    private BoardCommentResponse(Long boardCommentId, String content, BoardResponse boardResponse, UserResponse userResponse, List<BoardSubCommentResponse> boardSubComments, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.boardCommentId = boardCommentId;
        this.content = content;
        this.boardResponse = boardResponse;
        this.userResponse = userResponse;
        this.boardSubComments = boardSubComments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 생성자 factory method
    public static BoardCommentResponse of(Long boardCommentId, String content, BoardResponse boardResponse, UserResponse userResponse, List<BoardSubCommentResponse> boardSubComments, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardCommentResponse(
                boardCommentId, content, boardResponse, userResponse, boardSubComments, createdAt, updatedAt
        );
    }

    // dto -> response 변환 메서드
    public static BoardCommentResponse fromDto(BoardCommentDto dto) {
        return new BoardCommentResponse(
                dto.boardCommentId(),
                dto.content(),
                BoardResponse.fromDto(dto.boardDto()),
                UserResponse.fromDto(dto.userDto()),
                dto.boardSubComments().stream()
                        .map(BoardSubCommentResponse::fromDto)
                        .toList(),
                dto.createdAt(),
                dto.updatedAt()

        );
    }



}
