package com.jinan.profile.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 이 예외는 AJAX 요청을 처리하는 컨트롤러 메서드나 클래스에 적용된다.
 * 클래스 상단에 @RestController를 사용하거나, 메서드에 @ResponseBody 어노테이션을 추가하면 적용된다.
 */
@Slf4j
@RestControllerAdvice
public class AjaxControllerAdvice {

    @ExceptionHandler(ProfileApplicationException.class)
    public ResponseEntity<Map<String, String>> handleAjaxProfileApplicationException(ProfileApplicationException e) {
        log.error("Ajax Error occurs : {}, message : {}", e.getErrorCode(), e.getErrorCode().getMessage());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("errorCode", e.getErrorCode().getCode());
        responseBody.put("errorMessage", e.getErrorCode().getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        log.error("Error occurs {}", e.toString());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("errorMessage", e.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
