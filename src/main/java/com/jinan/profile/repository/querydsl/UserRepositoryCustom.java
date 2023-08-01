package com.jinan.profile.repository.querydsl;

import com.jinan.profile.domain.user.User;

public interface UserRepositoryCustom {

    User findByUserId(Long userId);

}
