package com.jinan.profile.dto.codes;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [공통 코드] API 통신에 대한 '에러 코드'를 Enum 형태로 관리를 한다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessCode {

    SELECT(200, "200", "SELECT SUCCESS"),      // 조회 성공 코드 (HTTP Response: 200 OK)
    DELETE(200, "200", "DELETE SUCCESS"),      // 삭제 성공 코드 (HTTP Response: 200 OK)
    SEND(200, "200", "SEND SUCCESS"),          // 전송 성공 코드 (HTTP Response: 200 OK)
    INSERT(201, "201", "INSERT SUCCESS"),      // 삽입 성공 코드 (HTTP Response: 201 Created)
    UPDATE(204, "204", "UPDATE SUCCESS"),      // 수정 성공 코드 (HTTP Response: 201 Created)
    ;

    private int status;       // 성공 코드의 '코드 상태'를 반환한다.
    private String code;      // 성공 코드의 '코드 값'을 반환한다.
    private String message;   // 성공 코드의 '코드 메시지'를 반환한다.

    // 생성자 구성
    SuccessCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
