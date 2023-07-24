package com.jinan.profile.config;

import com.jinan.profile.dto.security.Principal;
import com.jinan.profile.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

@Configuration
public class SecurityConfig {

    /**
     * Spring security6부터는 @Bean으로 filterChain을 등록하는 방식이 권장된다.
     * 그리고 기존에 사용하던 문법인 anyMatchers, MvcRequestMatchers는 requestMatchers로 통일되었다.
     */
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/resources/**").permitAll()         // "/resources/**" 경로에 대한 모든 요청을 허용한다.
                                .requestMatchers("/admin/**").access(this::isAdmin)   // "/admin/**" 경로에 대한 요청은 "ADMIN" 역할을 가진 사용자만 접근할 수 있게한다.
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/"
                                ).permitAll()
//                    .requestMatchers("/").hasRole("USER")                 // 또한, "/" 경로는 "USER" 역할을 가진 사용자만 접근할 수 있도록 설정하였다.
                                .anyRequest().authenticated()                           // 그 외의 모든 요청은 인증된 사용자만 접근할 수 있도록 설정하였다.
                )
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                ).build();
    }

    /**
     * isAdmin 메소드는 Supplier<Authentication>와 RequestAuthorizationContext를 인자로 받아서 "ADMIN" 역할을 가진 사용자인지 확인한다.
     * 만약 사용자가 "ADMIN" 역할을 가지고 있다면, AuthorizationDecision 객체는 true를 반환하고, 그렇지 않다면 false를 반환한다.
     */
    private AuthorizationDecision isAdmin(
            Supplier<Authentication> authenticationSupplier,
            RequestAuthorizationContext requestAuthorizationContext) {
        return new AuthorizationDecision(
                authenticationSupplier.get()
                        .getAuthorities()
                        .contains(new SimpleGrantedAuthority("ADMIN"))
        );
    }

    /**
     * 이녀석은 db를 기반으로 인증정보를 불러와서 user정보를 return하는 역할을 한다.
     * OAuth2에도 이런역할 하는게 필요한데 이걸 사용할수는 없어서 따로 만들어서 @Bean으로 등록해준다.
     */
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> userService
                .searchUser(username)
                .map(Principal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - userId: " + username));
    }

    /**
     * password 인코더 구현
     * spring security 암호화 모듈을 사용한다.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
