package com.jinan.profile.dto.board;

import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.controller.user.request.UserRequest;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.dto.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link BoardComment}
 */
public record BoardCommentDto(
        Long boardCommentId,
        String content,
        BoardDto boardDto,
        UserDto userDto,
        List<BoardSubCommentDto> boardSubComments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method 선언
    public static BoardCommentDto of(Long boardCommentId, String content, BoardDto boardDto, UserDto userDto, List<BoardSubCommentDto> boardSubComments, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardCommentDto(
                boardCommentId, content, boardDto, userDto, boardSubComments, createdAt, updatedAt
        );
    }

    // entity를 받아서 dto를 만들어주는 코드
    public static BoardCommentDto fromEntity(BoardComment entity) {

        // 순환참조가 안되도록 잘 확인하자
        List<BoardSubCommentDto> boardSubCommentsList = entity.getBoardSubComments()
                .stream()
                .map(BoardSubCommentDto::fromEntity)
                .collect(Collectors.toList());

        return BoardCommentDto.of(
                entity.getId(),
                entity.getContent(),
                BoardDto.fromEntity(entity.getBoard()),
                UserDto.fromEntity(entity.getUser()),
                boardSubCommentsList,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // request -> dto 변환 메서드
    public static BoardCommentDto fromRequest(BoardCommentRequest request) {
        return new BoardCommentDto(
                request.getBoardCommentId(),
                request.getContent(),
                BoardDto.fromRequest(request.getBoardRequest()),
                UserDto.fromRequest(request.getUserRequest()),
                request.getBoardSubComments()
                        .stream()
                        .map(BoardSubCommentDto::fromRequest)
                        .toList(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }

}