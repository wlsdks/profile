package com.jinan.profile.repository;

import com.jinan.profile.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatroomId(Long chatroomId);
}
