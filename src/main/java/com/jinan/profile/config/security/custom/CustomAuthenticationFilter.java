package com.jinan.profile.config.security.custom;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinan.profile.dto.user.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 아이디와 비밀번호 기반의 데이터를 Form 데이터로 전송을 받아 '인증'을 담당하는 필터이다.
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(
            AuthenticationManager authenticationManager
    ) {
        super(authenticationManager);
    }

    /**
     * 지정된 url로 form을 전송했을 경우 파라미터의 정보를 가져온다.
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authRequest;

        try {
            authRequest = getAuthRequest(request);
            setDetails(request, authRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Request로 받은 ID, PW를 기반으로 토큰을 발급한다.
     */
    private UsernamePasswordAuthenticationToken getAuthRequest(
            HttpServletRequest request
    ) throws Exception {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

            UserDto user = objectMapper.readValue(request.getInputStream(), UserDto.class);
            log.debug("1.CustomAuthenticationFilter :: username: " + user.username() + "userPw: " + user.password());
            // ID, PW를 기반으로 토큰 발급
            return new UsernamePasswordAuthenticationToken(user.username(), user.password());
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

}
