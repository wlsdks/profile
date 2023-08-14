package com.jinan.profile.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커스텀 에러코드 작성
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 500 : Internal Server Error
     * *********************************************************************************************
     */

    // 유저 예외처리
    USER_ALREADY_EXIST(409, "1001", "이미 존재하는 유저입니다."),
    USER_NOT_FOUND(404, "1002", "유저를 찾을수 없습니다."),
    INVALID_PASSWORD(401, "1003", "패스워드가 틀렸습니다."),
    DUPLICATED_USER_NAME(409, "1004", "중복된 아이디입니다."),
    INVALID_TOKEN(401, "1005", "토큰이 유효하지 않습니다."),
    INVALID_PERMISSION(401, "1006", "접근권한이 없습니다."),
    INTERNAL_SERVER_ERROR(500, "1008", "서버에 에러가 발생했습니다."),

    // 채팅 예외처리
    CHATROOM_NOT_FOUND(404, "2001", "채팅방을 찾을수 없습니다."),
    CHATMAP_NOT_FOUND(404, "2001", "채팅방을 찾을수 없습니다."),

    // 로그인 예외처리
    TOKEN_NOT_VALID(404, "3001", "유효하지 않은 토큰입니다."),
    TOKEN_NOT_FOUND(404, "3002", "존재하지 않은 토큰입니다.."),

    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),                        // 잘못된 서버 요청
    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),    // @RequestBody 데이터 미 존재
    INVALID_TYPE_VALUE(400, "G003", " Invalid Type Value"),                         // 유효하지 않은 타입
    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),  // Request Parameter 로 데이터가 전달되지 않을 경우
    IO_ERROR(400, "G008", "I/O Exception"),                                  // 입력/출력 값이 유효하지 않음
    JSON_PARSE_ERROR(400, "G009", "JsonParseException"),                          // com.google.gson JSON 파싱 실패
    JACKSON_PROCESS_ERROR(400, "G010", "com.fasterxml.jackson.core Exception"),   // com.fasterxml.jackson.core Processing Error
    FORBIDDEN_ERROR(403, "G004", "Forbidden Exception"),                     // 권한이 없음
    NOT_FOUND_ERROR(404, "G005", "Not Found Exception"),                     // 서버로 요청한 리소스가 존재하지 않음
    NULL_POINT_ERROR(404, "G006", "Null Point Exception"),                   // NULL Point Exception 발생
    NOT_VALID_ERROR(404, "G007", "handle Validation Exception"),             // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_HEADER_ERROR(404, "G007", "Header에 데이터가 존재하지 않는 경우 "),   // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음

    AUTH_IS_NULL(200, "A404", "AUTH_IS_NULL"),                      // A404
    AUTH_TOKEN_FAIL(200, "A405", "AUTH_TOKEN_FAIL"),                // A405
    AUTH_TOKEN_INVALID(200, "A406", "AUTH_TOKEN_INVALID"),          // A406
    AUTH_TOKEN_NOT_MATCH(200, "A407", "AUTH_TOKEN_NOT_MATCH"),      // A407
    AUTH_TOKEN_IS_NULL(200, "A408", "AUTH_TOKEN_IS_NULL"),          // A408

    BUSINESS_EXCEPTION_ERROR(200, "B999", "Business Exception Error"),  // Business Exception Error
    INSERT_ERROR(200, "9999", "Insert Transaction Error Exception"),    // Transaction Insert Error
    UPDATE_ERROR(200, "9999", "Update Transaction Error Exception"),    // Transaction Update Error
    DELETE_ERROR(200, "9999", "Delete Transaction Error Exception"),    // Transaction Delete Error


    COMMENT_NOT_FOUND(404, "4120", "존재하지 않는 댓글입니다."),
    USER_NOT_AUTHORIZED(401, "4121", "허가받지 못한 다른 유저입니다."),
    INVALID_INPUT(404, "4222", "잘못된 입력값입니다.")

    ;


    private int status;
    private String code;
    private String message;
}
