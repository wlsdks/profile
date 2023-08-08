package com.jinan.profile.repository.querydsl.user;

import com.jinan.profile.domain.user.User;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> findByLoginId(String loginId);

}
