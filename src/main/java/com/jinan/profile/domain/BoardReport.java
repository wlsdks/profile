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
public class BoardReport extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardCommentReportId;      // pk

    @ToString.Exclude
    @JoinColumn(name = "reporter_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users reporter;                 // 신고자

    @ToString.Exclude
    @JoinColumn(name = "reported_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users reported;                 // 신고당한자

    @ToString.Exclude
    @JoinColumn(name = "boardCommentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardComment boardComment;      // 신고당한 댓글정보

    @Column(name = "reason")
    private String reason;

    // todo: 생성일자는 있는데 수정일자가 없다. -> 테이블안에

    // private 생성자 선언
    private BoardReport(Users reporter, Users reported, BoardComment boardComment, String reason) {
        this.reporter = reporter;
        this.reported = reported;
        this.boardComment = boardComment;
        this.reason = reason;
    }

    // 생성자 factory method 선언
    public BoardReport of(Users reporter, Users reported, BoardComment boardComment, String reason) {
        return new BoardReport(reporter, reported, boardComment, reason);
    }

    // equals & hashCode 최적화
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardReport that)) return false;
        return that.boardCommentReportId != null && Objects.equals(getBoardCommentReportId(), that.getBoardCommentReportId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoardCommentReportId());
    }

}
