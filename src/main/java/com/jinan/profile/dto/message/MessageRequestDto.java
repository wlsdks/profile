package com.jinan.profile.dto.message;

import lombok.Data;

@Data
public class MessageRequestDto {

    private Long userId;
    private Long chatRoomId;
    private String text;

}
