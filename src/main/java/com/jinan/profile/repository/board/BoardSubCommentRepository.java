package com.jinan.profile.repository.board;

import com.jinan.profile.domain.board.BoardSubComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardSubCommentRepository extends JpaRepository<BoardSubComment, Long> {

    @Query("select sc from BoardSubComment sc where sc.boardComment.board.id = :boardId")
    Page<BoardSubComment> findAllByBoardId(@Param("boardId") Long boardId, Pageable pageable);
}
