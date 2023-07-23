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
public class UserDetails extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userDetailId;

    @ToString.Exclude
    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    private String provider;

    private String providerId;

    // id, 생성일자, 수정일자는 알아서 추가됨
    private UserDetails(Users users, String provider, String providerId) {
        this.users = users;
        this.provider = provider;
        this.providerId = providerId;
    }

    // factory 메소드 of() 선언
    public static UserDetails of(Users users, String provider, String providerId) {
        return new UserDetails(users, provider, providerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetails that)) return false;
        return this.getUserDetailId() != null && Objects.equals(getUserDetailId(), that.getUserDetailId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserDetailId());
    }
}
