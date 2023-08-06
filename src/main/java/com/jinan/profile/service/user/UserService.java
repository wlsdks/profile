package com.jinan.profile.service.user;

import com.jinan.profile.controller.user.request.LoginRequestDto;
import com.jinan.profile.controller.user.request.SignUpRequestDto;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.UserRoleEnum;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    public void signUp(SignUpRequestDto requestDto) {

        // 아이디
        String loginId = requestDto.getLoginId();
        // 비밀번호
        String password = passwordEncoder.encode(requestDto.getPassword());
        // 유저 권한
        UserRoleEnum role = UserRoleEnum.valueOf(requestDto.getRole());

        User user = User.builder()
                .loginId(loginId)
                .password(password)
                .role(role)
                .build();

        userRepository.save(user);
    }

    /**
     * 로그인 기능
     */
    @Transactional
    public void login(LoginRequestDto requestDto, HttpServletResponse response) {

        Optional<User> optionalMember = userRepository.findByLoginId(requestDto.getLoginId());

        if (optionalMember.isEmpty()) {
            log.warn("회원이 존재하지 않음");
            throw new IllegalArgumentException("회원이 존재하지 않음");
        }

        User user = optionalMember.get();

        /*비밀번호 다름.*/
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            log.warn("비밀번호가 일치하지 않습니다.");
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


        /*토큰을 쿠키로 발급 및 응답에 추가*/
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER,
                jwtUtil.createToken(user.getLoginId(), user.getRole()));
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일 동안 유효
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setSecure(false);

        response.addCookie(cookie);

    }

    public UserDto findByUsername(String username) {
        return null;
    }
}
