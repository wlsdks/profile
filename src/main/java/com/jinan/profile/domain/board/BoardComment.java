package com.jinan.profile.domain.board;

import com.jinan.profile.domain.AuditingFields;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.dto.board.BoardCommentDto;
import jakarta.persistence.*;
import lombok.*;

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
    @Builder
    private BoardComment(Long id, Board board, User user, String content) {
        this.id = id;
        this.board = board;
        this.user = user;
        this.content = content;
    }

    // 생성자 factory method 선언
    public static BoardComment of(Long id, Board board, User user, String content) {
        return new BoardComment(id, board, user, content);
    }

    // 생성자 factory method 선언
    public static BoardComment of(Board board, User user, String content) {
        return new BoardComment(null, board, user, content);
    }

    public static BoardComment of(String content) {
        return new BoardComment(null, null, null, content);
    }

    // dto -> entity로 변환하는 메서드
    public static BoardComment fromDto(BoardCommentDto dto) {
        return new BoardComment(
                dto.boardCommentId(),
                Board.fromDto(dto.boardDto()),
                User.fromDto(dto.userDto()),
                dto.content()
        );
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


    public void changeUserAndBoard(User user, Board board) {
        this.user = user;
        this.board = board;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
