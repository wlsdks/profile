package com.jinan.profile.domain.board;

import com.jinan.profile.domain.AuditingFields;
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
public class BoardComment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_id")
    private Long id;        // pk

    @ToString.Exclude
    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;    // 게시글

    @ToString.Exclude
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;    // 유저

    @Column(name = "content")
    private String content; // 내용

    @ToString.Exclude
    @OneToMany(mappedBy = "boardComment")
    private List<BoardSubComment> boardSubComments = new ArrayList<>(); // 대댓글

    // 생성자 private 선언
    private BoardComment(Board board, User user, String content) {
        this.board = board;
        this.user = user;
        this.content = content;
    }

    // 생성자 factory method 선언
    public static BoardComment of(Board board, User user, String content) {
        return new BoardComment(board, user, content);
    }

    // equals and hashCode 최적화
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardComment that)) return false;
        return that.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
