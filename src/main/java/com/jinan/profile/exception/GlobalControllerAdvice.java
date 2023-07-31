package com.jinan.profile.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 중앙 집중 예외처리를 위한 GlobalControllerAdvice 작성
 */
@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * ProfileApplicationException 발생했을 때 호출되는 메서드
     * 에러 페이지를 보여주고, 에러 메시지를 모델에 추가한다.
     * <p th:text="${errorMessage}"></p> 이런식으로 타임리프에 에러페이지를 만들고 가져다 사용한다.
     */
    @ExceptionHandler(ProfileApplicationException.class)
    public ModelAndView handleProfileApplicationException(ProfileApplicationException e) {
        log.error("Error occurs {}", e.toString());

        // Spring MVC는 알아서 'error'라는 이름의 뷰를 찾아서 렌더링한다.
        ModelAndView mav = new ModelAndView("error"); // 에러 페이지의 뷰 이름
        mav.addObject("errorMessage", e.getErrorCode().getMessage()); // 에러 메시지를 모델에 추가
        return mav;
    }

    /**
     * RuntimeException이 발생했을 때 호출되는 메서드
     * 에러 페이지를 보여주고, 에러 메시지를 모델에 추가한다.
     */
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException e) {
        log.error("Error occurs {}", e.toString());

        // Spring MVC는 알아서 'error'라는 이름의 뷰를 찾아서 렌더링한다.
        ModelAndView mav = new ModelAndView("error"); // 에러 페이지의 뷰 이름
        mav.addObject("errorMessage", e.getMessage()); // 에러 메시지를 모델에 추가
        return mav;
    }
}
