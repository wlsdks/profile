package com.jinan.profile.dto.codes;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseCode {

    private String code;
    private String message;  // 성공 코드의 '코드 메시지'를 반환한다.
    private int status;      // 성공 코드의 '코드 상태'를 반환한다.

    @Builder
    private ResponseCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
