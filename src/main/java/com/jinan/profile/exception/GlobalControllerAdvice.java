package com.jinan.profile.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 중앙 집중 예외처리를 위한 GlobalControllerAdvice 작성
 */
@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ProfileApplicationException.class)
    public ModelAndView handleProfileApplicationException(ProfileApplicationException e, WebRequest request) {
        // 발생한 예외에 대한 로그를 남김
        log.error("Error occurs : {}, message : {}", e.getErrorCode(), e.getErrorCode().getMessage());

        // 일반 웹 요청에 대한 응답을 구성
        ModelAndView mav = new ModelAndView("error"); // 에러 페이지의 뷰 이름
        mav.addObject("errorMessage", e.getErrorCode().getMessage()); // 에러 메시지를 모델에 추가

        // 에러 페이지를 반환
        return mav;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException e) {
        log.error("Error occurs {}", e.toString());

        // Spring MVC는 알아서 'error'라는 이름의 뷰를 찾아서 렌더링한다.
        ModelAndView mav = new ModelAndView("error"); // 에러 페이지의 뷰 이름
        mav.addObject("errorMessage", e.getMessage()); // 에러 메시지를 모델에 추가
        return mav;
    }

}
