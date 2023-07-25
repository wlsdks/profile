package com.jinan.profile.domain.user;

import com.jinan.profile.domain.AuditingFields;
import com.jinan.profile.domain.board.*;
import com.jinan.profile.domain.file.File;
import com.jinan.profile.domain.type.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Users extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;     // 유저pk

    @Column(name = "username", nullable = false)
    private String username; // 유저명

    @Column(name = "email", nullable = false)
    private String email;    // 이메일

    @Column(name = "password", nullable = false)
    private String password; // 패스워드

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType; // 계정 타입

    @ToString.Exclude
    @OneToMany(mappedBy = "users")
    private List<UserDetails> userDetails = new ArrayList<>();          // OAuth2 유저 상세정보

    @ToString.Exclude
    @OneToMany(mappedBy = "users")
    private List<Board> boards = new ArrayList<>();                     // 게시글

    @ToString.Exclude
    @OneToMany(mappedBy = "users")
    private List<BoardComment> boardComments = new ArrayList<>();       // 댓글

    @ToString.Exclude
    @OneToMany(mappedBy = "users")
    private List<BoardSubComment> boardSubComments = new ArrayList<>(); // 대댓글

    @ToString.Exclude
    @OneToMany(mappedBy = "reporter")
    private List<BoardReport> boardReporter = new ArrayList<>();        // 게시글 신고자 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "reported")
    private List<BoardReport> boardReported = new ArrayList<>();        // 게시글 신고당한사람 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "commentReporter")
    private List<BoardCommentReport> commentReporter = new ArrayList<>(); // 댓글 신고자 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "commentReported")
    private List<BoardCommentReport> commentReported = new ArrayList<>(); // 댓글 신고당한사람 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "subCommentReporter")
    private List<BoardSubCommentReport> subCommentReporter = new ArrayList<>(); // 대댓글 신고자 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "subCommentReported")
    private List<BoardSubCommentReport> subCommentReported = new ArrayList<>(); // 대댓글 신고당한사람 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "users")
    private List<File> files = new ArrayList<>(); // 파일


    // id, 생성일자, 수정일자는 자동으로 등록된다.
    private Users(String username, String email, String password, RoleType roleType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleType = roleType;
    }

    // factory 메소드 of() 생성
    public static Users of(String username, String email, String password, RoleType roleType) {
        return new Users(username, email, password, roleType);
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
        if (!(o instanceof Users users)) return false;
        return this.id != null && Objects.equals(getId(), users.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
