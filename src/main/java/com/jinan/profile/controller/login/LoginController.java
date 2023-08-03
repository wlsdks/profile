package com.jinan.profile.controller.login;

import com.jinan.profile.config.security.jwt.TokenUtils;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    /**
     * [View] 로그인 페이지를 엽니다.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserDto userDto, HttpServletResponse response) {

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.loginId(),
                        userDto.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = TokenUtils.generateJwtToken(userDto);

        // Create a cookie
        /**
         * HttpOnly 쿠키는 XSS(Cross-Site Scripting) 공격을 방지하기 위해 사용되며, 이 쿠키는 JavaScript를 통해 액세스할 수 없습니다.
         */
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        // Create response
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("jwt", jwt);  // Add the JWT token to the response
        responseMap.put("redirectUrl", "/main/rootPage");

        // Return the response
        return ResponseEntity.ok(responseMap);
    }

    /**
     * [API] 사용자 리스트 조회
     */
    @PostMapping("/user")
    public ResponseEntity<ApiResponse<Object>> selectCodeList(@RequestBody UserDto userDto) {
        List<UserDto> selectUserList = userService.selectUserList(userDto);
        ApiResponse<Object> ar = ApiResponse.builder()
                .result(selectUserList)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMessage(SuccessCode.SELECT.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

}
