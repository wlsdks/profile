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
    private Long boardReportId;              // pk

    @ToString.Exclude
    // 이 외래키의 이름(name값)만 테이블명과 일치하면 된다. 하단의 필드명은 단순히 JPA에서 사용하기 위함이라 테이블과 관련이 없다.
    @JoinColumn(name = "reporter_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users reporter;                 // 신고자

    @ToString.Exclude
    @JoinColumn(name = "reported_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users reported;                 // 신고당한사람

    @ToString.Exclude
    @JoinColumn(name = "boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;                     // 신고당한사람이 작성한 댓글정보

    @Column(name = "reason")
    private String reason;                   // 신고사유

    // todo: 생성일자는 있는데 수정일자가 없다. -> 테이블안에

    // private 생성자 선언
    private BoardReport(Users reporter, Users reported, Board board, String reason) {
        this.reporter = reporter;
        this.reported = reported;
        this.board = board;
        this.reason = reason;
    }

    // 생성자 factory method 선언
    public BoardReport of(Users reporter, Users reported, Board board, String reason) {
        return new BoardReport(reporter, reported, board, reason);
    }

    // equals & hashCode 최적화
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardReport that)) return false;
        return this.boardReportId != null && Objects.equals(getBoardReportId(), that.getBoardReportId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoardReportId());
    }
}
