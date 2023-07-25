package com.jinan.profile.repository.querydsl;

import com.jinan.profile.domain.user.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.jinan.profile.domain.QUsers.users;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {


    private final JPAQueryFactory queryFactory;

    @Override
    public Users findByUsersId(Long userId) {
        // 사용할때 QUser를 static import하는것을 잊지말자
        return queryFactory.select(users)
                .from(users)
                .where(users.userId.eq(userId))
                .fetchOne();
    }

}
