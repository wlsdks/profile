package com.jinan.profile.repository;

import com.jinan.profile.domain.message.ChatMap;
import com.jinan.profile.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMapRepository extends JpaRepository<ChatMap, Long> {

    Optional<ChatMap> findByUsers(Users users);

}