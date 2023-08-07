package com.jinan.profile.repository.querydsl.board;

import com.jinan.profile.domain.board.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    Optional<List<Board>> findByUserId(String loginId);
}
