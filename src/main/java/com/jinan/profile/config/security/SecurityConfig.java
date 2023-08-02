package com.jinan.profile.config.security;

import com.jinan.profile.config.security.custom.CustomAuthFailureHandler;
import com.jinan.profile.config.security.custom.CustomAuthSuccessHandler;
import com.jinan.profile.config.security.custom.CustomAuthenticationFilter;
import com.jinan.profile.config.security.custom.CustomAuthenticationProvider;
import com.jinan.profile.config.security.jwt.JwtAuthorizationFilter;
import com.jinan.profile.dto.security.SecurityUserDetailsDto;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.repository.UserRepository;
import com.jinan.profile.service.security.SecurityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Collections;
import java.util.function.Supplier;

@Slf4j
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
            HttpSecurity http,
            CustomAuthenticationFilter customAuthenticationFilter,
            JwtAuthorizationFilter jwtAuthorizationFilter
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
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class) // jwtAuthorizationFilter() 직접 작성해야함
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // customAuthenticationFilter 직접 작성해야함
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                ).build();
    }

    /**
     * 1. 커스텀을 수행한 '인증' 필터로 접근 URL, 데이터 전달방식(form) 등 인증 과정 및 인증 후 처리에 대한 설정을 구성하는 메서드다.
     * @return CustomAuthenticationFilter
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/user/login");     // 접근 URL
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());    // '인증' 성공 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler());    // '인증' 실패 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    /**
     * 2. authenticate 의 인증 메서드를 제공하는 매니져로'Provider'의 인터페이스를 의미한다.
     * 과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
     */
    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthenticationProvider) {
        return new ProviderManager(Collections.singletonList(customAuthenticationProvider));
    }

    /**
     * 3. '인증' 제공자로 사용자의 이름과 비밀번호가 요구된다.
     * 과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(SecurityUserService securityUserService) {
        return new CustomAuthenticationProvider(
                userDetailsService(securityUserService),
                bCryptPasswordEncoder()
        );
    }

    /**
     * 3-1. 시큐리티의 UserDetailsService를 구현한 메서드 사용자 정보를 통해 구분해서 로그인했는지 안했는지 판단한다.
     */
    @Bean
    public UserDetailsService userDetailsService(SecurityUserService securityUserService) {
        return username -> {
            UserDto userDto = UserDto.of(username);

            // 사용자 정보가 존재하지 않는 경우
            if (username == null || username.equals("")) {
                return securityUserService.login(userDto)
                        .map(user -> new SecurityUserDetailsDto(
                                user,
                                Collections.singleton(new SimpleGrantedAuthority(user.username()))))
                        .orElseThrow(() -> new AuthenticationServiceException(username));
            } else {  // 비밀번호가 틀린 경우
                return securityUserService.login(userDto)
                        .map(user -> new SecurityUserDetailsDto(
                                user,
                                Collections.singleton(new SimpleGrantedAuthority(user.username()))))
                        .orElseThrow(() -> new BadCredentialsException(username));
            }
        };
    }

    /**
     * 3-2. 유저정보를 받아오는 메서드
     */
    @Bean
    public SecurityUserService securityUserService(UserRepository userRepository) {
        return userDto -> userRepository
                .findByUsername(userDto.username())
                .map(UserDto::fromEntity);
    }

    /**
     * 3-3. 비밀번호를 암호화하기 위한 BCrypt 인코딩을 통하여 비밀번호에 대한 암호화를 수행한다.
     */
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 4. Spring Security 기반의 사용자의 정보가 맞을 경우 수행이 되며 결과값을 리턴해주는 Handler
     */
    @Bean
    public CustomAuthSuccessHandler customLoginSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }

    /**
     * 5. Spring Security 기반의 사용자의 정보가 맞지 않을 경우 수행이 되며 결과값을 리턴해주는 Handler
     */
    @Bean
    public CustomAuthFailureHandler customLoginFailureHandler() {
        return new CustomAuthFailureHandler();
    }

    /**
     * JWT 토큰을 통하여서 사용자를 인증합니다.
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


}


