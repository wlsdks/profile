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
@RequestMapping("api/v1/test")
@RestController
public class TokenController {

    /**
     * 사용자 정보를 기반으로 JWT를 발급하는 API
     * @param userDto UserDto
     * @return ApiResponseWrapper<ApiResponse> : 응답 결과 및 응답 코드 반환
     */
    @PostMapping("/generateToken")
    public ResponseEntity<ApiResponse> selectCodeList(@RequestBody UserDto userDto) {

        String resultToken = TokenUtils.generateJwtToken(userDto);

        ApiResponse<Object> response = ApiResponse.builder()
                .result(AuthConstants.TOKEN_TYPE + " " + resultToken)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMessage(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
