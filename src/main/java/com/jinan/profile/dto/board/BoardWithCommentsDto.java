package com.jinan.profile.dto.board;

import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.board.BoardSubComment;
import com.jinan.profile.dto.user.UsersDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link BoardSubComment}
 */
public record BoardWithCommentsDto(
        Long id,
        UsersDto users,
        List<BoardCommentDto> boardComments,
        String title,
        String content,
        int views,
        int likes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // 생성자 factory method 생성
    public static BoardWithCommentsDto of(
            Long id,
            UsersDto users,
            List<BoardCommentDto> boardComments,
            String title,
            String content,
            int views,
            int likes,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new BoardWithCommentsDto(id, users, boardComments, title, content, views, likes, createdAt, updatedAt);
    }

    // entity를 받아서 dto로 변환시켜주는 메서드
    public static BoardWithCommentsDto fromEntity(Board entity) {

        List<BoardCommentDto> boardCommentsDto = entity.getBoardComments()
                .stream()
                .map(BoardCommentDto::fromEntity)
                .collect(Collectors.toList());

        return BoardWithCommentsDto.of(
                entity.getId(),
                UsersDto.fromEntity(entity.getUsers()),
                boardCommentsDto,
                entity.getTitle(),
                entity.getContent(),
                entity.getViews(),
                entity.getLikes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}