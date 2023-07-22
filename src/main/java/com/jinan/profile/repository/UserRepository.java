package com.jinan.profile.repository;

import com.jinan.profile.domain.Users;
import com.jinan.profile.repository.querydsl.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

//querydsl custom 인터페이스로 extends에 추가해준다.
public interface UserRepository extends
        JpaRepository<Users, Long>
        , UserRepositoryCustom
{
}
