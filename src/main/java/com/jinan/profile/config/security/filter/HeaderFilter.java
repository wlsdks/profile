//package com.jinan.profile.config.security.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//
//
///**
// * HeaderFilter라는 이름의 Servlet 필터를 정의한다.
// * Servlet 필터는 클라이언트의 요청이 서버에 도달하기 전에 특정 작업을 수행하거나, 서버의 응답이 클라이언트에게 도달하기 전에 특정 작업을 수행하는데 사용된다.
// * HeaderFilter는 HTTP 응답 헤더에 CORS(Cross-Origin Resource Sharing) 관련 설정을 추가하는 역할을 한다. CORS는 다른 도메인으로부터의 자원 요청을 허용하는 메커니즘이다.
// */
//@Slf4j
//public class HeaderFilter implements Filter {
//
//    /**
//     * 이 코드는 클라이언트의 요청이 서버에 도달하기 전에 CORS 관련 설정을 HTTP 응답 헤더에 추가하는 필터를 정의하고 있다.
//     */
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
//
//        log.debug("HeaderFilter - doFilter method started"); // 메서드 시작 로그
//
//        // 1. ServletResponse를 HttpServletResponse로 형변환한다. 이렇게 하면 HTTP 응답에 특화된 메서드를 사용할 수 있다.
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        // 2. HTTP 응답 헤더에 특정 값을 설정한다. 이 코드에서는 CORS 관련 설정을 추가하고 있다.
//        res.setHeader("Access-Control-Allow-Origin", "*"); // 모든 도메인의 요청을 허용한다.
//        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"); // 서버가 허용하는 HTTP메서드를 지정한다.
//        res.setHeader("Access-Control-Max-Age", "3600"); // 사전 요청의 결과를 캐시할 시간을 지정한다.
//        res.setHeader(
//                "Access-Control-Allow-Headers", // 서버가 허용하는 HTTP요청 헤더를 지정한다.
//                "X-Requested-With, Content-Type, Authorization, X-XSRF-token"
//        );
//        res.setHeader("Access-Control-Allow-Credentials", "false"); // 이 헤더는 자격 증명을 포함한 요청을 허용할지를 지정한다. false는 허용하지 않는다는 의미다.
//
//        try {
//            log.debug("HeaderFilter - Before chain.doFilter"); // chain.doFilter 호출 전 로그
//            chain.doFilter(request, response); // 필터 체인의 다음 필터로 요청과 응답을 전달한다.
//            log.debug("HeaderFilter - After chain.doFilter"); // chain.doFilter 호출 후 로그
//        } catch (Exception e) {
//            log.error("HeaderFilter - Error during chain.doFilter", e); // 에러 발생 시 로그
//            throw e; // 에러를 다시 던져서 처리를 중단하거나 다른 필터/인터셉터에서 처리하도록 합니다.
//        }
//
//        log.debug("HeaderFilter - doFilter method finished"); // 메서드 종료 로그
//    }
//
//
//}
