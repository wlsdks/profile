package com.jinan.profile.domain.board;

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
public class BoardSubComment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_sub_comment_id")
    private Long id;

    @ToString.Exclude
    @JoinColumn(name = "board_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardComment boardComment;

    @ToString.Exclude
    @JoinColumn(name = "user_id")
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
        return that.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
