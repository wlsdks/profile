package com.jinan.profile.config.security.custom;

import com.jinan.profile.dto.security.SecurityUserDetailsDto;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 커스텀 AuthenticationProvider를 구현한 메서드
     * 사용자 인증을 처리한다.
     * 사용자가 제공한 인증 정보(여기서는 사용자 이름과 비밀번호)를 받아서 검증하고, 인증이 성공하면 인증된 사용자의 정보를 담은 Authentication 객체를 반환한다.
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("2.CustomAuthenticationProvider");

        /**
         * UsernamePasswordAuthenticationToken은 Spring Security에서 제공하는 Authentication 구현체 중 하나로,
         * 사용자 이름과 비밀번호를 기반으로 인증을 처리하는데 사용된다.
         * 이 클래스의 인스턴스는 사용자가 로그인 폼에서 제출한 사용자 이름과 비밀번호를 담고 있다.
         */
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // AuthenticationFilter에서 생성된 토큰으로부터 ID, PW를 조회
        String username = token.getName();
        String userPassword = (String) token.getCredentials();

        // Spring security - UserDetailsService를 통해 DB에서 username으로 사용자 조회
        SecurityUserDetailsDto securityUserDetailsDto = (SecurityUserDetailsDto) userDetailsService.loadUserByUsername(username);

        if (!(securityUserDetailsDto.getPassword().equalsIgnoreCase(userPassword) && securityUserDetailsDto.getPassword().equalsIgnoreCase(userPassword))) {
            throw new BadCredentialsException(securityUserDetailsDto.getUsername() + "Invalid password");
        }

        /**
         * 인증이 성공하면 인증된 사용자의 정보와 권한을 담은 새로운 UsernamePasswordAuthenticationToken을 반환한다.
         */
        return new UsernamePasswordAuthenticationToken(securityUserDetailsDto, userPassword, securityUserDetailsDto.getAuthorities());
    }

    /**
     * 이 AuthenticationProvider가 특정 Authentication 타입을 지원하는지 여부를 반환한다.
     * 여기서는 UsernamePasswordAuthenticationToken 클래스를 지원한다.
     * @param authentication
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
