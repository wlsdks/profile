package com.jinan.profile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing // auditing기능 추가(활성화 시킴)
@Configuration
public class JpaConfig {

    /**
     * auditing 할때 누가 추가, 수정했는지 사람의 이름을 설정해준다.
     * spring security를 통해 정보를 가지고 온다.
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        /**
         * SecurityContextHolder에 접근해서 인증정보로부터 작성자의 정보(getUsername)를 가지고 온다.
         */
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(BoardPrincipal.class::cast)
                .map(BoardPrincipal::getUsername);
    }

}
