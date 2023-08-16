package com.jinan.profile.repository.querydsl.user;

import com.jinan.profile.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.jinan.profile.domain.user.QUser.user;


@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {


    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findUserByLoginId(String loginId) {
        return Optional.ofNullable(queryFactory.select(user)
                .from(user)
                .where(user.loginId.eq(loginId))
                .fetchOne());
    }
}
