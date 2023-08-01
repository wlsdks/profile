package com.jinan.profile.dto.board;

import com.jinan.profile.domain.board.BoardReport;
import com.jinan.profile.dto.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.jinan.profile.domain.board.BoardReport}
 */
public record BoardReportDto(
        Long id,
        String reason,
        List<UserDto> reporter,    // 1개의 게시글 신고자는 여러명일수있으니 List
        UserDto reported,    // 1개의 게시글에서 신고당한 사람은 게시글 작성한 사람 한명이니까 컬렉션 x
        BoardDto board,             // 신고당한 1개의 게시글을 가져온다.
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method of 선언
    public static BoardReportDto of(Long id, String reason, List<UserDto> reporter, UserDto reported, BoardDto board, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BoardReportDto(
                id, reason, reporter, reported, board, createdAt, updatedAt
        );
    }

    // entity를 dto로 변환시켜주는 코드
    public static BoardReportDto fromEntity(BoardReport entity) {

        // 일반적인 Type은 map을 못쓰니까 일단 Optional로 만들어줬다. todo: 근데 이렇게까지 할 이유가있는지 알아보자 왜냐면 애초에 리스트로 안받으니까
        List<UserDto> reporterList = Optional.ofNullable(entity.getReporter())
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());

        return BoardReportDto.of(
                entity.getId(),
                entity.getReason(),
                reporterList,
                UserDto.fromEntity(entity.getReported()),
                BoardDto.fromEntity(entity.getBoard()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}