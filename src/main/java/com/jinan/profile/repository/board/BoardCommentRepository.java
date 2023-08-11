package com.jinan.profile.repository.board;

import com.jinan.profile.domain.board.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    @Query("select c from BoardComment c where c.board.id = :boardId")
    Optional<List<BoardComment>> findAllCommentsByBoardId(@Param("boardId") Long boardId);

}