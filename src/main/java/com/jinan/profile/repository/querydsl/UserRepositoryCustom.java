package com.jinan.profile.repository.querydsl;

import com.jinan.profile.domain.Users;

public interface UserRepositoryCustom {

    Users findByUsersId(Long userId);

}
