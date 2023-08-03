package com.jinan.profile.controller.login;

import com.jinan.profile.config.security.jwt.TokenUtils;
import com.jinan.profile.dto.codes.AuthConstants;
import com.jinan.profile.dto.codes.SuccessCode;
import com.jinan.profile.dto.response.ApiResponse;
import com.jinan.profile.dto.user.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("api/v1/token")
@RestController
public class TokenController {

    /**
     * 사용자 정보를 기반으로 JWT를 발급하는 API
     */
    @PostMapping("/generateToken")
    public ResponseEntity<ApiResponse> selectCodeList(@RequestBody UserDto userDto) {

        String resultToken = TokenUtils.generateJwtToken(userDto);

        ApiResponse ar = ApiResponse.builder()
                // BEARER {토큰} 형태로 반환을 해줍니다.
                .result(AuthConstants.TOKEN_TYPE + " " + resultToken)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMessage(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

}
