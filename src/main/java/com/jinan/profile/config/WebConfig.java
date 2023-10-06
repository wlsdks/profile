package com.jinan.profile.config;

import com.jinan.profile.config.security.interceptor.JwtTokenInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 동작설명:
 * 모든 요청은 먼저 HeaderFilter를 거치게 되며, 이 필터는 응답 헤더에 CORS 관련 설정을 추가한다.
 * 그리고 루트 URL에 접근하면 "/login"으로 리다이렉트되며, 정적 리소스는 지정된 경로에서 찾게 된다.
 */
@Slf4j
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/**") // 모든 URL에 대해 JWT 토큰 검사를 적용합니다.
                .excludePathPatterns("/user/login", "/login", "/main/rootPage", "/error.html", "/css/**", "/js/**", "/images/**"); // 로그인 페이지 및 정적 리소스 경로는 JWT 토큰 검사에서 제외합니다.
    }

}
