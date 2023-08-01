package com.jinan.profile.config.security;

import com.jinan.profile.config.security.jwt.JwtAuthorizationFilter;
import com.jinan.profile.dto.security.Principal;
import com.jinan.profile.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Collections;
import java.util.function.Supplier;

@Slf4j
//@EnableWebSecurity -> 이건 이제 안넣어도 된다.
@Configuration
public class SecurityConfig {

    /**
     * static(정적 자원)에 대해서는 시큐리티를 적용하지 않도록 한다.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * Spring security6부터는 @Bean으로 filterChain을 등록하는 방식이 권장된다.
     * 그리고 기존에 사용하던 문법인 anyMatchers, MvcRequestMatchers는 requestMatchers로 통일되었다.
     */
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {
        log.debug("[+] WebSecurityConfig Start !!! ");
        return http
                .csrf(AbstractHttpConfigurer::disable) // 서버에 인증정보를 저장하지 않기때문에 csrf를 사용하지 않음
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/token/**").permitAll()         // "/resources/**" 경로에 대한 모든 요청을 허용한다.
                        .requestMatchers("/create-room").permitAll()
                        .requestMatchers("/admin/**").access(this::isAdmin)   // "/admin/**" 경로에 대한 요청은 "ADMIN" 역할을 가진 사용자만 접근할 수 있게한다.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/"
                        ).permitAll()
                        .anyRequest().authenticated()                           // 그 외의 모든 요청은 인증된 사용자만 접근할 수 있도록 설정하였다.
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 이렇게 하면 JWT를 사용하는 OAuth2 리소스 서버를 설정할 수 있다.
                // Customizer.withDefaults()는 JWT 설정의 기본값을 사용하도록 지시한다.
                // 만약 커스텀 JWT 설정이 필요하다면, Customizer.withDefaults() 대신에 적절한 설정을 제공해야 한다.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class) // jwtAuthorizationFilter() 직접 작성해야함
                .formLogin(AbstractHttpConfigurer::disable)
//                .formLogin(Customizer.withDefaults())
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // customAuthenticationFilter 직접 작성해야함
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                ).build();
    }

    /**
     * authenticate 의 인증 메서드를 제공하는 매니져로'Provider'의 인터페이스를 의미한다.
     * 과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(customAuthenticationProvider()));
    }

    /**
     * '인증' 제공자로 사용자의 이름과 비밀번호가 요구된다.
     *  과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(bCryptPasswordEncoder());
    }

    /**
     * 비밀번호를 암호화하기 위한 BCrypt 인코딩을 통하여 비밀번호에 대한 암호화를 수행한다.
     */
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 6. 커스텀을 수행한 '인증' 필터로 접근 URL, 데이터 전달방식(form) 등 인증 과정 및 인증 후 처리에 대한 설정을 구성하는 메서드입니다.
     *
     * @return CustomAuthenticationFilter
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/user/login");     // 접근 URL
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());    // '인증' 성공 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler());    // '인증' 실패 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    /**
     * 7. Spring Security 기반의 사용자의 정보가 맞을 경우 수행이 되며 결과값을 리턴해주는 Handler
     *
     * @return CustomLoginSuccessHandler
     */
    @Bean
    public CustomAuthSuccessHandler customLoginSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }

    /**
     * 8. Spring Security 기반의 사용자의 정보가 맞지 않을 경우 수행이 되며 결과값을 리턴해주는 Handler
     *
     * @return CustomAuthFailureHandler
     */
    @Bean
    public CustomAuthFailureHandler customLoginFailureHandler() {
        return new CustomAuthFailureHandler();
    }

    /**
     * 9. JWT 토큰을 통하여서 사용자를 인증합니다.
     *
     * @return JwtAuthorizationFilter
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

    /**
     * isAdmin 메소드는 Supplier<Authentication>와 RequestAuthorizationContext를 인자로 받아서 "ADMIN" 역할을 가진 사용자인지 확인한다.
     * 만약 사용자가 "ADMIN" 역할을 가지고 있다면, AuthorizationDecision 객체는 true를 반환하고, 그렇지 않다면 false를 반환한다.
     */
    private AuthorizationDecision isAdmin(
            Supplier<Authentication> authenticationSupplier,
            RequestAuthorizationContext requestAuthorizationContext
    ) {
        return new AuthorizationDecision(
                authenticationSupplier.get()
                        .getAuthorities()
                        .contains(new SimpleGrantedAuthority("ADMIN"))
        );
    }

    /**
     * userDetailsService 빈은 username을 인자로 받아 UserService를 통해 사용자를 검색하고, 검색된 사용자를 Principal::from을 통해 Principal 객체로 변환한다.
     * 만약 사용자를 찾지 못하면 UsernameNotFoundException을 발생시킨다.
     * 이 방법은 UserDetailsService 인터페이스를 직접 구현하는 방법과 기본적으로 동일한 작업을 수행하지만, 빈으로 등록함으로써 UserDetailsService의 구현을 더 유연하게 관리할 수 있다.
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
