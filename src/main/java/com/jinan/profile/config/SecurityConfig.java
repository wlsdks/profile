package com.jinan.profile.config;

import com.jinan.profile.config.security.jwt.JwtAuthFilter;
import com.jinan.profile.config.security.jwt.JwtUtil;
import com.jinan.profile.domain.user.constant.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // resources 자원 접근을 허용한다.
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        log.debug("[+] WebSecurityConfig Start !!! ");
        // csrf 설정 해제
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers(HttpMethod.GET, "/user/signup").permitAll() // 회원가입 페이지 허용
                        .requestMatchers("/login").permitAll() // 로그인 페이지 허용
//                        .requestMatchers(HttpMethod.GET, "/user/api/signup").permitAll() // 회원가입 요청도 무조건 허용
//                        .requestMatchers(HttpMethod.GET, "/user/api/login").permitAll() // 로그인 요청도 무조건 허용
//                        .requestMatchers("/cookie/test").permitAll()
//                        .requestMatchers("/user/vip").hasRole(UserRoleEnum.VIP_MEMBER.toString()) // vip멤버 전용 페이지
//                        .requestMatchers("/user/admin").hasRole(UserRoleEnum.ADMIN.toString()) // 관리자 전용 페이지
                        .anyRequest().authenticated() // 나머지 요청은 전부 인증되어야 접속가능하다.
                ) // 회원가입 페이지는 무조건 허용한다.
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(login -> login.loginPage("/login").permitAll())
//                .exceptionHandling(exception -> exception.accessDeniedPage("/forbidden"))
                .build();
    }

}


