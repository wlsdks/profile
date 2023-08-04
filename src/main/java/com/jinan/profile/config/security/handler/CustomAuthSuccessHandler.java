package com.jinan.profile.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jinan.profile.config.security.jwt.TokenUtils;
import com.jinan.profile.dto.codes.AuthConstants;
import com.jinan.profile.dto.security.SecurityUserDetailsDto;
import com.jinan.profile.dto.user.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Component
@Configuration
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * 이 메서드는 HTTP 요청, HTTP 응답, 그리고 인증 객체를 인자로 받는다.
     * 인증 객체에서 사용자 정보를 가져와 JSON 형태로 변환하고, 이를 클라이언트에 응답한다.
     * 코드를 살펴보면, 사용자의 상태에 따라 응답을 다르게 구성하고 있다. 사용자의 상태가 '휴먼 상태'인 경우와 그렇지 않은 경우에 대해 각각 다른 응답을 구성하고 있다.
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        log.debug("3.CustomLoginSuccessHandler");

        // 1. 사용자와 관련된 정보를 모두 조회한다.
        UserDto userDto = ((SecurityUserDetailsDto) authentication.getPrincipal()).getUserDto();

        // 2. 조회한 데이터를 JSONObject 형태로 파싱한다.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JSONObject userDtoObject = new JSONObject(objectMapper.writeValueAsString(userDto));

        HashMap<String, Object> responseMap = new HashMap<>();
        JSONObject jsonObject;

        // 3-1. 사용자의 상태가 '휴먼 상태' 인 경우에 응답값으로 전달할 데이터
        if (userDto.status().equals("D")) {
            responseMap.put("userInfo", userDtoObject);
            responseMap.put("resultCode", 9001);
            responseMap.put("token", null);
            responseMap.put("failMessage", "휴면 계정입니다.");
            jsonObject = new JSONObject(responseMap);
        }
        // 3-2. 사용자의 상태가 '휴먼 상태'가 아닌 경우에 응답값으로 전달할 데이터
        else {
            // 1. 일반 계정일 경우 데이터 세팅
            responseMap.put("userInfo", userDtoObject);
            responseMap.put("resultCode", 200);
            responseMap.put("failMessage", null);
            jsonObject = new JSONObject(responseMap);

            //TODO: 추후 JWT 발급에 사용
            String token = TokenUtils.generateJwtToken(userDto);
            jsonObject.put("token", token);
            response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);
        }

        // 4. 구성한 응답값을 전달한다.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try (PrintWriter printWriter = response.getWriter()){
            printWriter.print(jsonObject); // 최종 저장된 '사용자 정보', '사이트 정보'를 Front에 전달
            printWriter.flush();
        }
    }

}
