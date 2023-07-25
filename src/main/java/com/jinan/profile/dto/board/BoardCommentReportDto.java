package com.jinan.profile.dto.board;

import com.jinan.profile.domain.board.BoardCommentReport;
import com.jinan.profile.dto.user.UsersDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.jinan.profile.domain.board.BoardCommentReport}
 */
public record BoardCommentReportDto(
        Long id,
        String reason,
        List<UsersDto> commentReporter, // 신고자
        UsersDto commentReported, // 신고당한자
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // 생성자 factory method of 선언
    public static BoardCommentReportDto of(Long id, String reason, List<UsersDto> commentReporter, UsersDto commentReported, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardCommentReportDto(id, reason, commentReporter, commentReported, createdAt, updatedAt);
    }

    // entity를 받아서 dto로 만들어주는 factory method 선언
    public static BoardCommentReportDto fromEntity(BoardCommentReport entity) {

        List<UsersDto> commentReporterList = Optional.ofNullable(entity.getCommentReporter())
                .stream()
                .map(UsersDto::fromEntity)
                .collect(Collectors.toList());

        return BoardCommentReportDto.of(
                entity.getId(),
                entity.getReason(),
                commentReporterList,
                UsersDto.fromEntity(entity.getCommentReported()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}