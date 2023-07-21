package com.jinan.profile.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private User user;

    private String provider;

    private String providerId;

    // id, 생성일자, 수정일자는 알아서 추가됨
    private UserDetails(User user, String provider, String providerId) {
        this.user = user;
        this.provider = provider;
        this.providerId = providerId;
    }

    // factory 메소드 of() 선언
    public static UserDetails of(User user, String provider, String providerId) {
        return new UserDetails(user, provider, providerId);
    }


}
