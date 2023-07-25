package com.jinan.profile.domain.file;

import com.jinan.profile.domain.AuditingFields;
import com.jinan.profile.domain.board.Board;
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
public class File extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;   // pk

    @ToString.Exclude
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;  // 유저정보

    @ToString.Exclude
    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;  // 게시글 정보

    @Column(name = "file_name")
    private String fileName; // 파일명

    @Column(name = "file_url")
    private String fileUrl;  // 파일url

    // 생성자 선언
    private File(Users users, Board board, String fileName, String fileUrl) {
        this.users = users;
        this.board = board;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    // 생성자 factory method 선언
    public static File of(Users users, Board board, String fileName, String fileUrl) {
        return new File(users, board, fileName, fileUrl);
    }

    // equals & hashCode 최적화
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File file)) return false;
        return this.id != null && Objects.equals(getId(), file.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
