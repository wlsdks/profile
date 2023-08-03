package com.jinan.profile.config.security.jwt;

import com.jinan.profile.dto.user.UserDto;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 관련 토큰 Util
 */
@Slf4j
public class TokenUtils {

    //    @Value(value = "${custom.jwt-secret-key}")
//    private static final String jwtSecretKey = "exampleSecretKey";
    private static final String jwtSecretKey = "thisIsASecretKeyUsedForJwtTokenGenerationAndItIsLongEnoughToMeetTheRequirementOf256Bits";


    /**
     * 사용자 pk를 기준으로 JWT 토큰을 발급하여 반환해 준다.
     */
    public static String generateJwtToken(UserDto userDto) {
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())                                  // Header 구성
                .setClaims(createClaims(userDto))                           // Payload - Claims구성
                .setSubject(String.valueOf(userDto.userId()))               // Payload - Subjects구성
                .signWith(SignatureAlgorithm.HS256, createSignature())      // Signature 구성
                .setExpiration(createExpiredDate());                        // Expired Date 구성

        return builder.compact();
    }

    /**
     * 토큰을 기반으로 사용자의 정보를 반환해주는 메서드
     */
    public static boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFormToken(token);

            log.info("expireTime : " + claims.getExpiration());
            log.info("loginId : " + claims.get("loginId"));
            log.info("username : " + claims.get("username"));

            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("Token Expired");
            return false;
        } catch (JwtException jwtException) {
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException npe) {
            log.error("Token is null");
            return false;
        }
    }

    /**
     * Header 내에 토큰을 추출한다.
     * @param header 헤더
     * @return String
     */
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    /**
     * 토큰의 만료기간을 지정하는 함수
     * @return Calendar
     */
    private static Date createExpiredDate() {
        // 토큰의 만료기간은 30일로 지정
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 8); // 8시간
        //c.add(Calendar.DATE,1); // 1일
        return c.getTime();
    }

    /**
     * JWT의 헤더값을 생성해주는 메서드
     */
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    /**
     * 사용자 정보를 기반으로 클래임을 생성해주는 메서드
     * @param userDto 사용자 정보
     * @return Map<String, Object>
     */
    private static Map<String, Object> createClaims(UserDto userDto) {
        // 공개 클래임에 사용자의 이름과 이메일을 설정해서 정보를 조회할 수 있다.
        Map<String, Object> claims = new HashMap<>();

        log.info("loginId : " + userDto.loginId());
        log.info("username : " + userDto.username());

        claims.put("loginId", userDto.loginId());
        claims.put("username", userDto.username());
        return claims;
    }

    /**
     * JWT 서명(Signature) 발급을 해주는 메서드
     * @return Key
     */
    private static Key createSignature() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * 토큰 정보를 기반으로 Claims 정보를 반환받는 메서드
     * @return Claims : Claims
     */
    private static Claims getClaimsFormToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                .parseClaimsJws(token).getBody();
    }

    /**
     * 토큰을 기반으로 사용자 정보를 반환받는 메서드
     * @return String : 사용자 아이디
     */
    public static String getUserIdFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.get("userId").toString();
    }

}

