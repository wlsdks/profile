package com.jinan.profile.controller.login;

import com.jinan.profile.dto.codes.SuccessCode;
import com.jinan.profile.dto.response.ApiResponse;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {


    /**
     * [View] 로그인 페이지를 엽니다.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * [Action] 로그인 프로세스를 동작시킨다.
     */
    @PostMapping("/user/login")
    public ResponseEntity<?> authenticateUser() {
        return ResponseEntity.ok().build();
    }

    /**
     * [Action] 로그아웃 프로세스를 동작시킨다.
     * 이 로그아웃은 cookie 값을 삭제함으로써 jwt토큰을 없애서 동작하도록 구성하였다.
     */
    @GetMapping("/user/logout")
    public String logout(HttpServletResponse response) {
        // JWT 토큰을 저장하는 쿠키의 값을 삭제
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setMaxAge(0);  // 쿠키의 유효기간을 0으로 설정하여 즉시 삭제
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return "redirect:/login";  // 로그인 페이지로 리다이렉트
    }


}
