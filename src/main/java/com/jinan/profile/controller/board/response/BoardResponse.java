package com.jinan.profile.controller.board.response;

import com.jinan.profile.dto.board.BoardDto;
import com.jinan.profile.dto.user.UserDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class BoardResponse {

    Long boardId;
    String title;
    String content;
    int views;
    int likes;
    UserDto userDto;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Builder
    private BoardResponse(Long boardId, String title, String content, int views, int likes, UserDto userDto, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.views = views;
        this.likes = likes;
        this.userDto = userDto;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // response를 만드는 factory method
    public static BoardResponse of(Long boardId, String title, String content, int views, int likes, UserDto userDto, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardResponse(
                boardId, title, content, views, likes, userDto, createdAt, updatedAt
        );
    }

    // dto를 response로 만드는 factory method
    public static BoardResponse fromDto(BoardDto dto) {
        return new BoardResponse(
                dto.boardId(),
                dto.title(),
                dto.content(),
                dto.views(),
                dto.likes(),
                dto.userDto(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }

}
