package com.jinan.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRequest {
    /** 송신자 id */
    @NotNull
    private Long senderId;

    /** 수신자 id */
    @NotNull
    private Long receiverId;

    /** 채팅방 id */
    @NotNull
    private Long roomId;

    /** 메시지 내용 */
    @NotBlank
    private String message;

    // 생성자
    public ChatRequest(@NotNull Long senderId, @NotNull Long receiverId, @NotNull Long roomId, String message) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.roomId = roomId;
        this.message = message;
    }
}
