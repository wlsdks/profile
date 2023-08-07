package com.jinan.profile.controller.login;

import com.jinan.profile.config.security.jwt.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/token")
public class TokenController {

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        boolean isValid = TokenUtils.isValidToken(token); // TokenUtils에서 토큰 유효성 검사 메서드 호출

        Map<String, Boolean> response = new HashMap<>();
        response.put("isValid", isValid);

        return ResponseEntity.ok(response);
    }
}
