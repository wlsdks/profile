package com.jinan.profile.service.board;

import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;

    /**
     * 유저가 댓글을 작성하면 저장된다.
     */
    public BoardCommentDto createComment(BoardCommentRequest request, Long boardId) {
        // todo: 1.일단 저장할때 작성된 게시글의 boardId와 연관을 시켜줘야하니 request안의 BoardRequest 그리고 그 안의 id 값에 boardId를 넣어주고 계속 map으로 변환한다.
        request.getBoardRequest().setId(boardId);

        // todo: 2. 데이터를 저장하기 위해 request를 entity로 변환한다.
        BoardComment boardComment = Optional.of(request)
                .map(BoardCommentDto::fromRequest)
                .map(BoardComment::fromDto)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        return BoardCommentDto.fromEntity(boardCommentRepository.save(boardComment));
    }

    /**
     * 게시글의 id를 통해 연관된 모든 댓글을 가져온다.
     */
    public List<BoardCommentDto> getBoardComment(Long boardId) {

        return boardCommentRepository.findAllCommentsByBoardId(boardId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.BAD_REQUEST_ERROR))
                .stream()
                .map(BoardCommentDto::fromEntity)
                .collect(Collectors.toList());
    }



}
