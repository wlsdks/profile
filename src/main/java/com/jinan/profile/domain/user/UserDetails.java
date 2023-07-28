package com.jinan.profile.domain.user;

import com.jinan.profile.domain.AuditingFields;
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
    @Column(name = "user_details_id")
    private Long id;                // pk

    @ToString.Exclude
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;            // 유저

    @Column(name = "provider")
    private String provider;        // 제공업체(kakao, google, apple)

    @Column(name = "provider_id")
    private String providerId;      // 제공업체 고유id

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
        return this.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
