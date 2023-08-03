package com.jinan.profile.config.security.filter;

import com.jinan.profile.config.security.jwt.TokenUtils;
import com.jinan.profile.dto.codes.AuthConstants;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 지정한 URL별 JWT의 유효성 검증을 수행하며 직접적인 사용자 인증을 확인합니다.
 */
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. 토큰이 필요하지 않은 API URL에 대해서 배열로 구성한다.
        List<String> list = Arrays.asList(
                "/api/v1/user/login",
                "/api/v1/token/generateToken"
        );

        // 2. 토큰이 필요하지 않은 API URL의 경우 -> 로직 처리없이 다음 필터로 이동한다.
        if (list.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. OPTIONS 요청일 경우 -> 로직 처리 없이 다음 필터로 이동
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        // [STEP.1] Client에서 API를 요청할때 Header를 확인한다.
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        log.debug("[+] header Check: " + header);

        try {
            // [STEP.2-1] Header 내에 토큰이 존재하는 경우
            if (header != null && !header.equalsIgnoreCase("")) {

                // [STEP.2-2] Header 내에있는 토큰을 추출한다.
                String token = TokenUtils.getTokenFromHeader(header);

                // [STEP.2-3] 추출한 토큰이 유효한지 여부를 체크한다.
                if (TokenUtils.isValidToken(token)) {

                    // [STEP.2-4] 추출한 토큰을 기반으로 사용자 아이디를 반환받는다.
                    String loginId = TokenUtils.getUserIdFromToken(token);
                    log.debug("[+] loginId Check: " + loginId);

                    // [STEP.2-5] 사용자 아이디가 존재하는지에 대한 여부를 체크한다.
                    if (loginId != null && !loginId.equalsIgnoreCase("")) {
                        filterChain.doFilter(request, response);
                    } else {
                        throw new ProfileApplicationException(ErrorCode.USER_NOT_FOUND);
                    }
                }
                // [STEP.2-6[ 토큰이 유효하지 않은 경우
                else {
                    throw new ProfileApplicationException(ErrorCode.TOKEN_NOT_VALID);
                }
            }
            // [STEP.3] 토큰이 존재하지 않는 경우
            else {
                throw new ProfileApplicationException(ErrorCode.TOKEN_NOT_FOUND);
            }
        } catch (Exception e) {
            // Token 내에 Exception이 발생 하였을 경우 => 클라이언트에 응답값을 반환하고 종료합니다.
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            JSONObject jsonObject = jsonResponseWrapper(e);
            printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();
        }
    }

    /**
     * 토큰 관련 Exception 발생 시 예외 응답값 구성
     */
    private JSONObject jsonResponseWrapper(Exception e) {

        String resultMessage = "";
        // JWT 토큰 만료
        if (e instanceof ExpiredJwtException) {
            resultMessage = "TOKEN Expired";
        }
        // JWT 허용된 토큰이 아님
        else if (e instanceof SignatureException) {
            resultMessage = "TOKEN SignatureException Login";
        }
        // JWT 토큰내에서 오류 발생 시
        else if (e instanceof JwtException) {
            resultMessage = "TOKEN Parsing JwtException";
        }
        // 이외 JTW 토큰내에서 오류 발생
        else {
            resultMessage = "OTHER TOKEN ERROR";
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("code", "9999");
        jsonMap.put("message", resultMessage);
        jsonMap.put("reason", e.getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);
        logger.error(resultMessage, e);
        return jsonObject;
    }

}
