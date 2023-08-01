package com.jinan.profile.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [공통] API Response 결과의 반환 값을 관리
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private T result;               // API 응답 결과 Response
    private int resultCode;         // API 응답 코드 Response
    private String resultMessage;   // API 응답 코드 Message

    @Builder
    private ApiResponse(final T result, final int resultCode, final String resultMessage) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

}
