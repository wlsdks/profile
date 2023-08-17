package com.jinan.profile.repository.board;

import com.jinan.profile.domain.board.BoardSubComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardSubCommentRepository extends JpaRepository<BoardSubComment, Long> {

    Page<BoardSubComment> findAllById(Long boardId, Pageable pageable);
}
