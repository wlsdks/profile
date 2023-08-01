package com.jinan.profile.repository.querydsl;

import com.jinan.profile.domain.user.QUser;
import com.jinan.profile.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.jinan.profile.domain.user.QUser.user;


@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {


    private final JPAQueryFactory queryFactory;

    @Override
    public User findByUserId(Long userId) {
        // 사용할때 QUser를 static import하는것을 잊지말자
        return queryFactory.select(user)
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();
    }

}
