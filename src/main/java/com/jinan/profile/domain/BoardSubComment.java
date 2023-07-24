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
public class BoardSubComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardSubCommentId;

    @ToString.Exclude
    @JoinColumn(name = "boardCommentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardComment boardComment;

    @ToString.Exclude
    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    private String content;

    // 생성자 선언
    private BoardSubComment(BoardComment boardComment, Users users, String content) {
        this.boardComment = boardComment;
        this.users = users;
        this.content = content;
    }

    // 생성자 factory method 선언
    public BoardSubComment of(BoardComment boardComment, Users users, String content) {
        return new BoardSubComment(boardComment, users, content);
    }

    // equals & hashCode 최적화
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardSubComment that)) return false;
        return that.boardSubCommentId != null && Objects.equals(getBoardSubCommentId(), that.getBoardSubCommentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoardSubCommentId());
    }
}
