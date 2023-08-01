package com.jinan.profile.repository;

import com.jinan.profile.domain.user.User;
import com.jinan.profile.repository.querydsl.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//querydsl custom 인터페이스로 extends에 추가해준다.
public interface UserRepository extends
        JpaRepository<User, Long>
        , UserRepositoryCustom
{
    Optional<User> findByUsername(String username);
}
