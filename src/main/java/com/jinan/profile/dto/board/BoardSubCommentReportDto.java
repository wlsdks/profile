package com.jinan.profile.dto.board;

import com.jinan.profile.domain.board.BoardSubComment;
import com.jinan.profile.domain.board.BoardSubCommentReport;
import com.jinan.profile.dto.user.UsersDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.jinan.profile.domain.board.BoardSubCommentReport}
 */
public record BoardSubCommentReportDto(
        Long id,
        String reason,
        List<UsersDto> subCommentReporter,
        UsersDto subCommentReported,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // 생성자 factory method of 선언
    public static BoardSubCommentReportDto of(Long id, String reason, List<UsersDto> subCommentReporter, UsersDto subCommentReported, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardSubCommentReportDto(id, reason, subCommentReporter, subCommentReported, createdAt, updatedAt);
    }

    // entity를  받아서 dto로 변환하는 코드
    public static BoardSubCommentReportDto fromEntity(BoardSubCommentReport entity) {

        List<UsersDto> subCommentReporterList = Optional.ofNullable(entity.getSubCommentReporter())
                .stream()
                .map(UsersDto::fromEntity)
                .collect(Collectors.toList());

        return BoardSubCommentReportDto.of(
                entity.getId(),
                entity.getReason(),
                subCommentReporterList,
                UsersDto.fromEntity(entity.getSubCommentReported()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }


}