package com.jinan.profile.repository;

import com.jinan.profile.domain.Users;
import com.jinan.profile.repository.querydsl.UserRepositoryCustom;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

//querydsl custom 인터페이스로 extends에 추가해준다.
public interface UserRepository extends
        JpaRepository<Users, Long>
        , UserRepositoryCustom
{
    Optional<Users> findByUsername(String username);
}
