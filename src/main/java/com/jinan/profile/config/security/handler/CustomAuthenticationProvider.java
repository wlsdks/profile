package com.jinan.profile.config.security.handler;

import com.jinan.profile.dto.security.SecurityUserDetailsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 3. '인증' 제공자로 사용자의 이름과 비밀번호가 요구된다.
 * 이 메서드는 사용자 정의 인증 제공자를 생성한다. 인증 제공자는 사용자 이름과 비밀번호를 사용하여 인증을 수행한다.
 * 과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;


    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 커스텀 AuthenticationProvider를 구현한 메서드
     * 이 메서드는 사용자 인증을 처리한다.
     * Authentication 객체를 받아 사용자 이름과 비밀번호를 추출하고, UserDetailsService를 사용하여 사용자 정보를 로드하고, 로드된 사용자의 비밀번호와 사용자가 제출한 비밀번호를 비교하여 인증을 처리한다.
     * 인증이 성공하면 인증된 사용자의 정보와 권한을 담은 UsernamePasswordAuthenticationToken 객체를 반환하고, 인증이 실패하면 BadCredentialsException을 던진다.
     * @Request 여기에는 CustomAuthenticationFilter클래스의 attemptAuthentication의 반환값이 들어온다.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("2.CustomAuthenticationProvider");

        /**
         * UsernamePasswordAuthenticationToken은 Spring Security에서 제공하는 Authentication 구현체 중 하나로, 사용자 이름과 비밀번호를 기반으로 인증을 처리하는데 사용된다.
         * 이 클래스의 인스턴스는 사용자가 로그인 폼에서 제출한 사용자 이름과 비밀번호를 담고 있다.
         */
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // AuthenticationFilter에서 생성된 토큰으로부터 ID, PW를 조회
        String loginId = token.getName();
        String userPassword = (String) token.getCredentials();

        // Spring security - UserDetailsService를 통해 DB에서 username으로 사용자 조회
        SecurityUserDetailsDto securityUserDetailsDto = (SecurityUserDetailsDto) userDetailsService.loadUserByUsername(loginId);

        // 대소문자를 구분하는 matches() 메서드로 db와 사용자가 제출한 비밀번호를 비교
        if (!bCryptPasswordEncoder().matches(userPassword, securityUserDetailsDto.getUserDto().password())) {
            throw new BadCredentialsException(securityUserDetailsDto.getUsername() + "Invalid password");
        }

        // 인증이 성공하면 인증된 사용자의 정보와 권한을 담은 새로운 UsernamePasswordAuthenticationToken을 반환한다.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(securityUserDetailsDto, userPassword, securityUserDetailsDto.getAuthorities());
        return authToken;
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
