package com.jinan.profile.config.security.custom;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 사용자의 '인증'에 대해 실패하였을 경우 수행되는 Handler로 실패에 대한 사용자에게 반환값을 구성하여 전달한다.
 */
@Slf4j
@Configuration
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        // [STEP.1] 클라이언트로 전달할 응답값을 구성한다.
        JSONObject jsonObject = new JSONObject();
        String failMessage = "";

        // [STEP.2] 발생한 Exception에 대해서 확인한다.
        if (exception instanceof AuthenticationServiceException) {
            failMessage = "로그인 정보가 일치하지 않습니다.";
        } else if (exception instanceof LockedException) {
            failMessage = " 로그인 정보가 일치하지 않습니다.";
        } else if (exception instanceof DisabledException) {
            failMessage = " 로그인 정보가 일치하지 않습니다.";
        } else if (exception instanceof AccountExpiredException) {
            failMessage = " 로그인 정보가 일치하지 않습니다.";
        } else if (exception instanceof CredentialsExpiredException) {
            failMessage = " 로그인 정보가 일치하지 않습니다.";
        }

        // [STEP.3] 응답값을 구성하고 전달한다.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();

        log.debug(failMessage);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("userInfo", null);
        resultMap.put("resultCode", 9999);
        resultMap.put("failMessage", failMessage);
        jsonObject = new JSONObject(resultMap);

        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();
    }

}
