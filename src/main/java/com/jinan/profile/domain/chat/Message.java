package com.jinan.profile.domain.chat;

import com.jinan.profile.domain.AuditingFields;
import com.jinan.profile.domain.user.Users;
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
public class Message extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;                    // pk

    @ToString.Exclude
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;                // 유저

    @ToString.Exclude
    @JoinColumn(name = "chatroom_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;          // 채팅방

    @Column(name = "text")
    private String text;                // 채팅내용

    // private 생성자 선언 -> factory method 생성을 위함
    private Message(Users users, ChatRoom chatRoom, String text) {
        this.users = users;
        this.chatRoom = chatRoom;
        this.text = text;
    }

    // 생성자 factory method of 선언
    public static Message of(Users users, ChatRoom chatRoom, String text) {
        return new Message(users, chatRoom, text);
    }

    // equals & hashCode 최적화
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message)) return false;
        return this.id != null && Objects.equals(getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
