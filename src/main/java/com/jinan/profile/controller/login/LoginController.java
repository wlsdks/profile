package com.jinan.profile.controller.login;

import com.jinan.profile.config.security.jwt.JwtToken;
import com.jinan.profile.dto.codes.SuccessCode;
import com.jinan.profile.dto.response.ApiResponse;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class LoginController {

    private final UserService userService;

    /**
     * 로그인 요청을 받는 메서드를 만든다.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtToken> loginSuccess(@RequestBody Map<String, String> loginForm) {

        JwtToken token = userService.login(loginForm.get("loginId"), loginForm.get("password"));
        return ResponseEntity.ok(token);
    }

//    @PostMapping("/user/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody UserDto userDto, HttpServletResponse response) {
//
//        // Authenticate user
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        userDto.loginId(),
//                        userDto.password()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Create response
//        Map<String, String> responseMap = new HashMap<>();
//        responseMap.put("redirectUrl", "/main/rootPage");
//
//        // Return the response
//        return ResponseEntity.ok(responseMap);
//    }


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
