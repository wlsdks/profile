package com.jinan.profile.domain.board;

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
public class BoardSubCommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_sub_comment_report_id")
    private Long id;

    @ToString.Exclude
    @JoinColumn(name = "board_sub_reporter_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users subCommentReporter;

    @ToString.Exclude
    @JoinColumn(name = "board_sub_reported_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users subCommentReported;

    @ToString.Exclude
    @JoinColumn(name = "board_sub_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardSubComment boardSubComment;

    @Column(name = "reason")
    private String reason;

    // 생성자 선언
    private BoardSubCommentReport(Users subCommentReporter, Users subCommentReported, BoardSubComment boardSubComment, String reason) {
        this.subCommentReporter = subCommentReporter;
        this.subCommentReported = subCommentReported;
        this.boardSubComment = boardSubComment;
        this.reason = reason;
    }

    // 생성자 factory method 선언
    public BoardSubCommentReport of(Users subCommentReporter, Users subCommentReported, BoardSubComment boardSubComment, String reason) {
        return new BoardSubCommentReport(subCommentReporter, subCommentReported, boardSubComment, reason);
    }

    // equals & hashCode 최적화
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardSubCommentReport that)) return false;
        return that.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
