//package com.jinan.profile.config.security.interceptor;
//
//import com.jinan.profile.config.security.jwt.TokenUtils;
//import com.jinan.profile.exception.ErrorCode;
//import com.jinan.profile.exception.ProfileApplicationException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Slf4j
//public class JwtTokenInterceptor implements HandlerInterceptor {
//
//    /**
//     * 이 코드는 JWT 토큰의 유효성을 검사하는 인터셉터로, JWT 토큰이 유효하지 않거나 존재하지 않는 경우에는 요청을 거부하고, 유효한 경우에는 요청을 통과시킨다.
//     * 이 메서드는 HTTP 요청이 컨트롤러에 도달하기 전에 실행된다.
//     * 이 메서드에서는 HTTP 요청의 헤더에서 "Authorization" 헤더를 가져와 JWT 토큰의 유효성을 검사한다.
//     * HTTP 요청 메서드가 "OPTIONS"인 경우에는 토큰 검사를 생략하고 요청을 통과시키고 있다. 이는 CORS(Cross-Origin Resource Sharing) 프리플라이트 요청을 처리하기 위한 것이다.
//     * 그리고 "Authorization" 헤더가 존재하는 경우에는 헤더에서 토큰을 추출하고, 토큰이 유효한지 검사한다. 토큰이 유효하면 요청을 통과시키고, 그렇지 않으면 예외를 발생시킨다.
//     * "Authorization" 헤더가 존재하지 않는 경우에도 예외를 발생시킨다. 이 예외는 ProfileApplicationException이며, 에러 코드를 인자로 받아서 생성된다.
//     */
//    @Override
//    public boolean preHandle(
//            HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull Object handler
//    ) {
//
//        String header = request.getHeader("AUTHORIZATION");
//        log.debug("AUTHORIZATION Header: " + header);
//
//        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
//            log.debug("if request options method is options, return true");
//
//            return true;
//        }
//
//        if (header != null) {
//
//            String token = TokenUtils.getTokenFromHeader(header);
//
//            if (TokenUtils.isValidToken(token)) {
//                String userId = TokenUtils.getUserIdFromToken(token);
//                if (userId == null) {
//                    log.debug("token isn't userId");
//                    throw new ProfileApplicationException(ErrorCode.AUTH_TOKEN_NOT_MATCH);
//                }
//                return true;
//            } else {
//                throw new ProfileApplicationException(ErrorCode.AUTH_TOKEN_INVALID);
//            }
//        } else {
//            throw new ProfileApplicationException(ErrorCode.AUTH_TOKEN_IS_NULL);
//        }
//    }
//
//}
