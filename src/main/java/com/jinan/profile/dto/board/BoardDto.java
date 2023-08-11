package com.jinan.profile.dto.board;

import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * DTO for {@link Board}
 */
public record BoardDto(
        Long boardId,
        String title,
        String content,
        int views,
        int likes,
        UserDto userDto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method of 선언
    public static BoardDto of(Long boardId, String title, String content, int views, int likes, UserDto userDto, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardDto(boardId, title, content, views, likes, userDto, createdAt, updatedAt);
    }

    // 서비스 레이어에서 entity를 dto로 변환시켜주는 코드
    public static BoardDto fromEntity(Board entity) {
        return BoardDto.of(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getViews(),
                entity.getLikes(),
                Optional.ofNullable(entity.getUser())
                        .map(UserDto::fromEntity)
                        .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND)),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // request -> dto 변환 메서드
    public static BoardDto fromRequest(BoardRequest request) {
        return of(
                request.getId(),
                request.getTitle(),
                request.getContent(),
                request.getViews(),
                request.getLikes(),
                UserDto.fromEntity(request.getUser()),
                null,
                null
        );
    }

}