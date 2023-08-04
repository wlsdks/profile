//package com.jinan.profile.config.security.jwt;
//
//import com.jinan.profile.dto.user.UserDto;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//
//import java.security.Key;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * JWT 관련 토큰 Util
// */
//@Slf4j
//public class TokenUtils {
//
//    //    @Value(value = "${custom.jwt-secret-key}")
////    private static final String jwtSecretKey = "exampleSecretKey";
//    // 상수
//    private static final String jwtSecretKey = "thisIsASecretKeyUsedForJwtTokenGenerationAndItIsLongEnoughToMeetTheRequirementOf256Bits";
//    private static final String JWT_TYPE = "JWT";
//    private static final String ALGORITHM = "HS256";
//    private static final String LOGIN_ID = "loginId";
//    private static final String USERNAME = "username";
//
//
//    /**
//     * 사용자 pk를 기준으로 JWT 토큰을 발급하여 반환해 준다.
//     */
//    public static String generateJwtToken(UserDto userDto) {
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generate a key
//
//        JwtBuilder builder = Jwts.builder()
//                .setHeader(createHeader())                                  // Header 구성
//                .setClaims(createClaims(userDto))                           // Payload - Claims구성
//                .setSubject(String.valueOf(userDto.loginId()))               // Payload - Subjects구성
//                .signWith(key, SignatureAlgorithm.HS256)                    // Signature 구성
//                .setExpiration(createExpiredDate());                        // Expired Date 구성
//
//        return builder.compact();
//    }
//    /**
//     * 토큰을 기반으로 사용자의 정보를 반환해주는 메서드
//     */
//    public static boolean isValidToken(String token) {
//        try {
//            Claims claims = getClaimsFormToken(token);
//
//            log.info("expireTime : " + claims.getExpiration());
//            log.info("loginId : " + claims.get(LOGIN_ID));
//            log.info("username : " + claims.get(USERNAME));
//
//            return true;
//        } catch (ExpiredJwtException expiredJwtException) {
//            log.error("Token Expired", expiredJwtException);
//            return false;
//        } catch (JwtException jwtException) {
//            log.error("Token Tampered", jwtException);
//            return false;
//        } catch (NullPointerException npe) {
//            log.error("Token is null", npe);
//            return false;
//        }
//    }
//
//    /**
//     * Header 내에 토큰을 추출한다.
//     * @param header 헤더
//     * @return String
//     */
//    public static String getTokenFromHeader(String header) {
//        return header.split(" ")[1];
//    }
//
//    /**
//     * 토큰의 만료기간을 지정하는 함수
//     * @return Date
//     */
//    private static Date createExpiredDate() {
//        // 토큰의 만료기간은 8시간으로 지정
//        Instant now = Instant.now();
//        Instant expiryDate = now.plus(Duration.ofHours(8));
//        return Date.from(expiryDate);
//    }
//
//    /**
//     * JWT의 헤더값을 생성해주는 메서드
//     */
//    private static Map<String, Object> createHeader() {
//        Map<String, Object> header = new HashMap<>();
//
//        header.put("typ", JWT_TYPE);
//        header.put("alg", ALGORITHM);
//        header.put("regDate", System.currentTimeMillis());
//        return header;
//    }
//
//    /**
//     * 사용자 정보를 기반으로 클래임을 생성해주는 메서드
//     * @param userDto 사용자 정보
//     * @return Map<String, Object>
//     */
//    private static Map<String, Object> createClaims(UserDto userDto) {
//        // 공개 클래임에 사용자의 이름과 이메일을 설정해서 정보를 조회할 수 있다.
//        Map<String, Object> claims = new HashMap<>();
//
//        log.info("loginId : " + userDto.loginId());
//        log.info("username : " + userDto.username());
//
//        claims.put(LOGIN_ID, userDto.loginId());
//        claims.put(USERNAME, userDto.username());
//        return claims;
//    }
//
//    /**
//     * 토큰 정보를 기반으로 Claims 정보를 반환받는 메서드
//     * @return Claims : Claims
//     */
//    private static Claims getClaimsFormToken(String token) {
//        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecretKey.getBytes()))
//                .build().parseClaimsJws(token).getBody();
//    }
//
//    /**
//     * 토큰을 기반으로 사용자 정보를 반환받는 메서드
//     * @return String : 사용자 아이디
//     */
//    public static String getUserIdFromToken(String token) {
//        Claims claims = getClaimsFormToken(token);
//        return claims.get(LOGIN_ID).toString();
//    }
//
//}
//
