package com.jinan.profile.domain;

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
public class Board extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String title;

    private String content;

    private int views;

    private int likes;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Users users;

    public void setUsers(Users users) {
        this.users = users;
    }

    // id, 생성일자, 수정일자는 자동으로 등록된다.
    private Board(String title, String content, int views, int likes) {
        this.title = title;
        this.content = content;
        this.views = views;
        this.likes = likes;
    }

    // factory 메소드
    public Board of(String title, String content, int views, int likes) {
        return new Board(title, content, views, likes);
    }

    // custom Equals & hashCode 선언
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; 
        if (!(o instanceof Board board)) return false;
        return this.boardId != null && Objects.equals(getBoardId(), board.getBoardId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoardId());
    }
}
