package com.jinan.profile.controller.login;

import com.jinan.profile.dto.codes.SuccessCode;
import com.jinan.profile.dto.response.ApiResponse;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserService userService;


    /**
     * [View] 로그인 페이지를 엽니다.
     */
    @PostMapping("/login")
    public String loginPage(Model model) {
        return "pages/login/loginPage";
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
