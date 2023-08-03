package com.jinan.profile.config;

import com.jinan.profile.config.security.filter.HeaderFilter;
import com.jinan.profile.config.security.interceptor.JwtTokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 동작설명:
 * 모든 요청은 먼저 HeaderFilter를 거치게 되며, 이 필터는 응답 헤더에 CORS 관련 설정을 추가한다.
 * 그리고 루트 URL에 접근하면 "/login"으로 리다이렉트되며, 정적 리소스는 지정된 경로에서 찾게 된다.
 */
@Slf4j
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /**
     *  이 배열은 정적 리소스가 위치할 수 있는 경로를 나열하고 있다. 이 경로들은 addResourceHandlers 메서드에서 사용된다.
     */
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/static/",
            "classpath:/public/",
            "classpath:/",
            "classpath:/resources/",
            "classpath:/META-INF/resources/",
            "classpath:/META-INF/resources/webjars/"
    };

    /**
     * 뷰 컨트롤러를 추가할때 사용된다. 여기서는 루트 URL("/")에 접근했을 때 "/login"으로 리다이렉트하도록 설정하고 있다.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        log.debug("[+] WebConfig Start !!! ");
        registry.addRedirectViewController("/", "/login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /**
     * 정적 리소스를 처리하는 핸들러를 추가하는데 사용된다.
     * 여기서는 모든 요청("/**")에 대해 CLASSPATH_RESOURCE_LOCATIONS에 지정된 경로에서 리소스를 찾도록 설정하고 있다.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    /**
     * HeaderFilter를 등록하는 빈을 생성한다.
     * FilterRegistrationBean을 사용하면 필터의 순서를 지정하고, 어떤 URL 패턴에 적용할지를 설정할 수 있다.
     * 여기서는 모든 요청("/*")에 대해 HeaderFilter를 적용하도록 설정하고 있다.
     */
    @Bean
    public FilterRegistrationBean<HeaderFilter> getFilterRegistrationBean() {
        FilterRegistrationBean<HeaderFilter> registrationBean = new FilterRegistrationBean<>(createHeaderFilter());
        registrationBean.setOrder(Integer.MIN_VALUE);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    /**
     * HeaderFilter의 인스턴스를 생성하는 빈을 정의한다.
     */
    @Bean
    public HeaderFilter createHeaderFilter() {
        return new HeaderFilter();
    }

    /**
     * JwtTokenInterceptor의 인스턴스를 생성하는 빈을 정의한다.
     * 이 인스턴스는 다른 설정에서 사용될 수 있다.
     */
    @Bean
    public JwtTokenInterceptor jwtTokenInterceptor() {
        return new JwtTokenInterceptor();
    }

}
