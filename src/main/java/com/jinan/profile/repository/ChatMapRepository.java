package com.jinan.profile.repository;

import com.jinan.profile.domain.message.ChatMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMapRepository extends JpaRepository<ChatMap, Long> {
}