package com.jinan.profile.domain.message;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    @Column(name = "chatroom_name")
    private String chatRoomName;

    // private 생성자 선언
    private ChatRoom(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    // factory method 선언
    public ChatRoom of(String chatRoomName) {
        return new ChatRoom(chatRoomName);
    }

    // equals & hashCode 최적화
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatRoom chatRoom)) return false;
        return this.id != null && Objects.equals(getId(), chatRoom.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
