package com.jinan.profile.config;

import com.jinan.profile.config.security.filter.CustomAuthenticationFilter;
import com.jinan.profile.config.security.filter.JwtAuthorizationFilter;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServlet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * 테스트에 필요한 security관련 설정을 여기서 전부 처리하기위해 만들었다.
 */
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize ->
                    authorize.anyRequest().permitAll()
            );
        return http.build();
    }


}
