package com.jinan.profile.service.board;

import com.jinan.profile.controller.board.request.BoardCommentRequest;
import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.board.BoardComment;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.dto.board.BoardCommentDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.board.BoardCommentRepository;
import com.jinan.profile.repository.board.BoardRepository;
import com.jinan.profile.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
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
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    private final BoardCommentRepository boardCommentRepository;

    /**
     * 유저가 댓글을 작성하면 저장된다.
     */
    public BoardCommentDto createComment(BoardCommentRequest request, Long boardId, String loginId) {

        // 1. boardId로 board를 꺼내서 BoardRequest에 넣어야 한다.
        BoardComment boardComment = BoardComment.of(request.getContent());

        // 1. boardComment에 board는 boardId로 받아와서 세팅한다.
        Board board = boardRepository
                .findById(boardId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        // 2. user는 loginId로 받아와서 세팅한다.
        User user = userRepository
                .findByLoginId(loginId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        // 3. user, board를 세팅한다.
        boardComment.changeUser(user);
        boardComment.changeBoard(board);

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
