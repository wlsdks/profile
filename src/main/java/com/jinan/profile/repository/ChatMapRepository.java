package com.jinan.profile.repository;

import com.jinan.profile.domain.chat.ChatMap;
import com.jinan.profile.domain.chat.ChatRoom;
import com.jinan.profile.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMapRepository extends JpaRepository<ChatMap, Long> {

//    Optional<ChatMap> findByUsers(Users users);

    Optional<ChatMap> findByUsersAndChatRoom(User user, ChatRoom chatRoom);

}