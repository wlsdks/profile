package com.jinan.profile.repository.querydsl.board;

import com.jinan.profile.domain.board.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.jinan.profile.domain.board.QBoard.board;
import static com.jinan.profile.domain.user.QUser.user;

/**
 * 쿼리dsl 구현체 코드
 */
@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<Board>> findByLoginId(String loginId) {
        List<Board> result = queryFactory
                .select(board)
                .from(board)
                .where(user.loginId.eq(loginId))
                .fetch();

        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }
}
