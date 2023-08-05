package com.jinan.profile.controller.user;

import com.jinan.profile.controller.user.request.LoginRequestDto;
import com.jinan.profile.controller.user.request.SignUpRequestDto;
import com.jinan.profile.service.user.UserService;
import com.jinan.profile.service.user.security.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserService userService;

    /**
     * index 뷰
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 로그인 뷰
     */
    @GetMapping("/login")
    public String loginView(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        /**
         * 이미 로그인된 사용자일 경우에는 인덱스 페이지로 강제이동시킨다.
         */
//        if (userDetails != null) {
//            log.info(userDetails.getUser().getLoginId() + "님이 로그인 페이지로 이동을 시도함. -> index페이지로 강제 이동시킴.");
//            return "redirect:/index";
//        }
        return "login";

    }

    /**
     * 회원가입 뷰
     */
    @GetMapping("/signup")
    public String signUp() {
        return "signUp";
    }

    /**
     * vip 전용 페이지
     */
    @GetMapping("/vip")
    public String vip(HttpServletRequest request) {
        return "thymeleaf/role";
    }

    /**
     * 관리자 페이지
     */
    @GetMapping("/admin")
    public String admin(HttpServletRequest request) {
        return "thymeleaf/role";
    }

    /**
     * 회원가입
     */
    @PostMapping("/api/signup")
    public String signUp(SignUpRequestDto requestDto) {
        userService.signUp(requestDto);
        return "redirect:/login";
    }

    /**
     * 로그인
     */
    @PostMapping("/api/login")
    public String login(LoginRequestDto requestDto, HttpServletResponse response) {
        userService.login(requestDto, response);
        return "redirect:/index";
    }

    /**
     * 서버 로그 쿠키 테스트 확인
     */
    @GetMapping("/cookie/test")
    public String test(@CookieValue(value = "Authorization", defaultValue = "", required = false) String test) {
        log.info(test);
        return "thymeleaf/index";
    }

    /**
     * 403 forbidden -> 인가실패시 동작
     */
    @GetMapping("/forbidden")
    public String forbidden() {
        log.warn("비정상적인 접근: 403 forbidden");
        // 따로 횟수를 기록한다.
        return "redirect:/index";
    }

    /**
     * 로그아웃 API
     */
    @GetMapping("/api/logout")
    public String logout(
            @CookieValue(value = "Authorization", defaultValue = "", required = false) Cookie jwtCookie,
            HttpServletResponse response
    )
    {
        // jwt 쿠키를 가지고와서 제거한다.
        jwtCookie.setValue(null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return "redirect:/login";
    }

}