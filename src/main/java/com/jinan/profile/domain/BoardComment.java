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
public class BoardComment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardCommentId;

    @ToString.Exclude
    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ToString.Exclude
    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "boardComment")
    private List<BoardSubComment> boardSubComments = new ArrayList<>();

    // 생성자 private 선언
    private BoardComment(Board board, Users users, String content) {
        this.board = board;
        this.users = users;
        this.content = content;
    }

    // 생성자 factory method 선언
    public BoardComment of(Board board, Users users, String content) {
        return new BoardComment(board, users, content);
    }

    // equals and hashCode 최적화
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardComment that)) return false;
        return that.boardCommentId != null && Objects.equals(getBoardCommentId(), that.getBoardCommentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoardCommentId());
    }
}
