package com.jinan.profile.controller.board.request;

import com.jinan.profile.dto.board.BoardCommentDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class BoardCommentRequest {

    String content;

    @Builder
    private BoardCommentRequest(String content) {
        this.content = content;
    }

    // dto -> request factory method 선언
    public static BoardCommentRequest fromDto(BoardCommentDto dto) {
        return new BoardCommentRequest(
                dto.content()
        );
    }

}
