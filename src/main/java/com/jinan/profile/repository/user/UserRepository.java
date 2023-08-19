package com.jinan.profile.repository.user;

import com.jinan.profile.domain.user.User;
import com.jinan.profile.repository.querydsl.user.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//querydsl custom 인터페이스로 extends에 추가해준다.
@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findUserByLoginId(String loginId);

}
