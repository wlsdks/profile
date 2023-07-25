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
public class BoardCommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_report_id")
    private Long id;             // pk

    @ToString.Exclude
    @JoinColumn(name = "comment_reporter_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users commentReporter;                 // 신고자

    @ToString.Exclude
    @JoinColumn(name = "comment_reported_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users commentReported;                 // 신고당한사람

    @ToString.Exclude
    @JoinColumn(name = "boardCommentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardComment boardComment;             // 신고당한사람이 작성한 댓글정보

    @Column(name = "reason")
    private String reason;                         // 신고사유

    // 생성자 선언
    private BoardCommentReport(Users commentReporter, Users commentReported, BoardComment boardComment, String reason) {
        this.commentReporter = commentReporter;
        this.commentReported = commentReported;
        this.boardComment = boardComment;
        this.reason = reason;
    }

    // 생성자 factory method 선언
    public static BoardCommentReport of(Users commentReporter, Users commentReported, BoardComment boardComment, String reason) {
        return new BoardCommentReport(commentReporter, commentReported, boardComment, reason);
    }

    // equals & hashCode 최적화
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardCommentReport that)) return false;
        return that.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
