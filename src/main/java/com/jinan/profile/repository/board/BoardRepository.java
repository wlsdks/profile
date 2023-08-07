package com.jinan.profile.repository.board;

import com.jinan.profile.domain.board.Board;
import com.jinan.profile.repository.querydsl.board.BoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//querydsl custom 인터페이스로 extends에 추가해준다.
@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {


}
