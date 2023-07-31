package com.jinan.profile.domain.chat;

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
public class MessageReport extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_report_id")
    private Long id;                    // pk

    @ToString.Exclude
    @JoinColumn(name = "reporter_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users messageReporter;      // 신고자

    @ToString.Exclude
    @JoinColumn(name = "message_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Message message;            // 메시지

    @Column(name = "reason")
    private String reason;              // 신고사유

    // private 생성자 선언
    private MessageReport(Users messageReporter, Message message, String reason) {
        this.messageReporter = messageReporter;
        this.message = message;
        this.reason = reason;
    }

    // factory method of 선언
    public MessageReport of(Users messageReporter, Message message, String reason) {
        return new MessageReport(messageReporter, message, reason);
    }

    // equals & hashCode 최적화
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageReport that)) return false;
        return that.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
