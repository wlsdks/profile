package com.jinan.profile.controller.board.request;

import com.jinan.profile.domain.board.Board;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.dto.board.BoardDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Setter
@Getter
public class BoardRequest {

    private Long id;            // pk
    private String title;       // 제목
    private String content;     // 내용
    private int views;          // 조회수
    private int likes;          // 좋아요 수
    private User user;        // 유저
    private MultipartFile file;

    @Builder
    private BoardRequest(Long id, String title, String content, int views, int likes, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.views = views;
        this.likes = likes;
        this.user = user;
    }

    // board 엔티티를 request로 변환하는 메서드
    public static BoardRequest of(Board board) {
        return BoardRequest.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .views(board.getViews())
                .likes(board.getLikes())
                .user(board.getUser())
                .build();

    }

}
