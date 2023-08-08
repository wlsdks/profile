package com.jinan.profile.domain.board;

import com.jinan.profile.controller.board.request.BoardRequest;
import com.jinan.profile.domain.AuditingFields;
import com.jinan.profile.domain.file.File;
import com.jinan.profile.domain.user.User;
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
    private Long id;            // pk

    @Column(name = "title")
    private String title;       // 제목

    @Column(name = "content")
    private String content;     // 내용

    @Column(name = "views")
    private int views;          // 조회수

    @Column(name = "likes")
    private int likes;          // 좋아요 수

    @ToString.Exclude
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;        // 유저

    @ToString.Exclude
    @OneToMany(mappedBy = "board")
    private List<BoardComment> boardComments = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "board")
    private List<File> files = new ArrayList<>();

    // id, 생성일자, 수정일자는 자동으로 등록된다.

    private Board(String title, String content, int views, int likes, User user) {
        this.title = title;
        this.content = content;
        this.views = views;
        this.likes = likes;
        this.user = user;
    }

    // 생성자 factory 메소드
    public static Board of(String title, String content, int views, int likes, User user) {
        return new Board(title, content, views, likes, user);
    }

    // request를 Board 엔티티로 변환시키는 메서드
    public static Board toRequest(BoardRequest request) {
        return of(
                request.getTitle(),
                request.getContent(),
                request.getViews(),
                request.getLikes(),
                request.getUser()
        );
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
