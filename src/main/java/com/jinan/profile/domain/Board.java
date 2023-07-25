package com.jinan.profile.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Board extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    private int views;

    private int likes;

    @ToString.Exclude
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @ToString.Exclude
    @OneToMany(mappedBy = "board")
    private List<BoardComment> boardComments = new ArrayList<>();

    // id, 생성일자, 수정일자는 자동으로 등록된다.
    private Board(String title, String content, int views, int likes, Users users, List<BoardComment> boardComments) {
        this.title = title;
        this.content = content;
        this.views = views;
        this.likes = likes;
        this.users = users;
        this.boardComments = boardComments;
    }

    // 생성자 factory 메소드
    public Board of(String title, String content, int views, int likes, Users users, List<BoardComment> boardComments) {
        return new Board(title, content, views, likes, users, boardComments);
    }

    // custom Equals & hashCode 선언
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board board)) return false;
        return this.id != null && Objects.equals(getId(), board.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
