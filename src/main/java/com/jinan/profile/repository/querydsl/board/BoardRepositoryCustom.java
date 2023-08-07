package com.jinan.profile.repository.querydsl.board;

import com.jinan.profile.domain.board.Board;

import java.util.List;

public interface BoardRepositoryCustom {

    List<Board> findByUserId(String loginId);
}
