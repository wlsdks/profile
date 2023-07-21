package com.jinan.profile.domain;

import com.jinan.profile.domain.type.RoleType;
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
public class User extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private RoleType roleType;

    // id, 생성시간, 수정시간은 자동으로 등록된다.
    private User(String username, String email, String password, RoleType roleType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleType = roleType;
    }

    // factory 메소드 of() 생성
    public static User of(String username, String email, String password, RoleType roleType) {
        return new User(username, email, password, roleType);
    }

    /**
     * lombok의 equalsAndHashCode대신에 cmd+n으로 직접 만들었다.
     * lombok으로 만든다면 모든필드를 검사하지만 이렇게 직접 id만 만들면 id값만으로 비교하기때문에 최적화가 가능하다.
     * 마지막 return문에 id != null && 를 맨앞에 붙여줘서 id가 부여되지 않았으면(아직 영속화가 안되었으면) 동등성 검사가 의미가 없다고 해주는것이다.
     * 만약 id가 있다면 id가 같은지만 보고 id가 같다면 같은 객체라고 동등성 검사를 하는것이다.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return this.getUser_id() != null && Objects.equals(getUser_id(), user.getUser_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser_id());
    }
}
