package com.jinan.profile.config;

import com.jinan.profile.config.security.filter.CustomAuthenticationFilter;
import com.jinan.profile.config.security.filter.JwtAuthorizationFilter;
import com.jinan.profile.config.security.handler.CustomAuthFailureHandler;
import com.jinan.profile.config.security.handler.CustomAuthSuccessHandler;
import com.jinan.profile.config.security.handler.CustomAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.AuthorizationDecision;
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
     * 이 메서드는 정적 자원에 대해 보안을 적용하지 않도록 설정한다.
     * 정적 자원은 보통 HTML, CSS, JavaScript, 이미지 파일 등을 의미하며, 이들에 대해 보안을 적용하지 않음으로써 성능을 향상시킬 수 있다.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * Spring security6부터는 @Bean으로 filterChain을 등록하는 방식이 권장된다.
     * 그리고 기존에 사용하던 문법인 anyMatchers, MvcRequestMatchers는 requestMatchers로 통일되었다.
     * 1.csrf(disable): csrf비활성화 -> 2.authorizeHttpRequests: 특정경로 요청제어
     * 3.sessionManagement(): SessionCreationPolicy.STATELESS로 설정하면 Spring Security는 세션을 생성하지 않고, 인증을 위해 세션을 사용하지 않는다. 이는 주로 토큰 기반의 인증 방식에서 사용된다.
     * 4.httpBasic(): HTTP Basic 인증을 활성화한다. HTTP Basic 인증은 클라이언트가 요청 헤더의 Authorization 필드에 사용자 이름과 비밀번호를 Base64 인코딩된 형태로 전송하는 인증 방식이다.
     * 5 addFilterBefore(): 이 설정은 특정 필터를 다른 필터 앞에 추가한다. 여기서는 JwtAuthorizationFilter를 BasicAuthenticationFilter 앞에, CustomAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가했다.
     * 이 필터들은 HTTP 요청이 들어올 때 실행되며, 요청을 처리하거나 다음 필터로 전달하거나, 요청을 거부할 수 있다.
     * 6. formLogin(AbstractHttpConfigurer::disable): 이 설정은 폼 기반 로그인을 비활성화한다. 폼 기반 로그인은 사용자가 로그인 폼을 통해 사용자 이름과 비밀번호를 전송하는 인증 방식이다.
     * 7. logout: 이 설정은 로그아웃 기능을 설정한다. 로그아웃 성공 시 "/"로 리다이렉트하도록 설정하였다.
     *
     * <p>기능 요약</p>
     * 사용자가 로그인을 시도하면 CustomAuthenticationFilter가 이를 처리한다.
     * 이 필터는 사용자가 제출한 사용자 이름과 비밀번호를 가지고 인증을 시도하고, 인증이 성공하면 인증된 사용자의 정보와 권한을 담은 Authentication 객체를 생성하여 SecurityContext에 저장한다.
     * 이후 HTTP 요청이 들어오면 JwtAuthorizationFilter가 이를 처리하며, 이 필터는 요청 헤더의 JWT 토큰을 검증하고, 토큰이 유효하면 토큰에서 사용자의 정보와 권한을 추출하여 SecurityContext에 저장한다.
     * 이렇게 SecurityContext에 저장된 사용자의 정보와 권한은 애플리케이션의 다른 부분에서 사용될 수 있다.
     */
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            CustomAuthenticationFilter customAuthenticationFilter,
            JwtAuthorizationFilter jwtAuthorizationFilter
    ) throws Exception {
        log.debug("[+] WebSecurityConfig Start !!! ");
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/main").authenticated()
                        .anyRequest().permitAll()
                )
                // 1. 먼저, JwtAuthorizationFilter가 실행되어 요청 헤더에서 JWT 토큰을 추출하고 이 토큰을 검증한다.
                .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                // 2. CustomAuthenticationFilter가 실행되어 사용자 이름과 비밀번호를 사용하여 인증을 수행한다. 이 필터는 주로 로그인 요청을 처리하는 데 사용된다.
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 1. 커스텀을 수행한 '인증' 필터로 접근 URL, 데이터 전달방식(form) 등 인증 과정 및 인증 후 처리에 대한 설정을 구성하는 메서드다.
     * 이 메서드는 사용자 정의 인증 필터를 생성한다. 이 필터는 로그인 요청을 처리하고, 인증 성공/실패 핸들러를 설정한다.
     *
     * @return CustomAuthenticationFilter
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(
            AuthenticationManager authenticationManager,
            CustomAuthSuccessHandler customAuthSuccessHandler,
            CustomAuthFailureHandler customAuthFailureHandler
    ) {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");     // 접근 URL
        customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthSuccessHandler);    // '인증' 성공 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthFailureHandler);    // '인증' 실패 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }


    /**
     * 2. authenticate 의 인증 메서드를 제공하는 매니져로'Provider'의 인터페이스를 의미한다.
     * 이 메서드는 인증 매니저를 생성한다. 인증 매니저는 인증 과정을 처리하는 역할을 한다.
     * 과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
     */
    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthenticationProvider) {
        return new ProviderManager(Collections.singletonList(customAuthenticationProvider));
    }

    /**
     * 3. '인증' 제공자로 사용자의 이름과 비밀번호가 요구된다.
     * 이 메서드는 사용자 정의 인증 제공자를 생성한다. 인증 제공자는 사용자 이름과 비밀번호를 사용하여 인증을 수행한다.
     * 과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(UserDetailsService userDetailsService) {
        return new CustomAuthenticationProvider(
                userDetailsService
        );
    }

    /**
     * 4. Spring Security 기반의 사용자의 정보가 맞을 경우 수행이 되며 결과값을 리턴해주는 Handler
     * customLoginSuccessHandler: 이 메서드는 인증 성공 핸들러를 생성한다. 인증 성공 핸들러는 인증 성공시 수행할 작업을 정의한다.
     */
//    @Bean
//    public CustomAuthSuccessHandler customLoginSuccessHandler() {
//        return new CustomAuthSuccessHandler();
//    }

    /**
     * 5. Spring Security 기반의 사용자의 정보가 맞지 않을 경우 수행이 되며 결과값을 리턴해주는 Handler
     * customLoginFailureHandler: 이 메서드는 인증 실패 핸들러를 생성한다. 인증 실패 핸들러는 인증 실패시 수행할 작업을 정의한다.
     */
//    @Bean
//    public CustomAuthFailureHandler customLoginFailureHandler() {
//        return new CustomAuthFailureHandler();
//    }

    /**
     * "JWT 토큰을 통하여서 사용자를 인증한다." -> 이 메서드는 JWT 인증 필터를 생성한다.
     * JWT 인증 필터는 요청 헤더의 JWT 토큰을 검증하고, 토큰이 유효하면 토큰에서 사용자의 정보와 권한을 추출하여 SecurityContext에 저장한다.
     */
//    @Bean
//    public JwtAuthorizationFilter jwtAuthorizationFilter() {
//        return new JwtAuthorizationFilter();
//    }

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


