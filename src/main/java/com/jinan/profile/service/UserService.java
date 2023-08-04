package com.jinan.profile.service;

import com.jinan.profile.config.security.jwt.JwtToken;
import com.jinan.profile.config.security.jwt.JwtTokenProvider;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * repository에서 가져오는 entity정보는 service 외부로는 나가지 않도록 dto를 사용한다.(격리를 시킨다.)
     * 이 메소드에서 마지막에 orElse 예외처리를 해주지 않은것은 이 service 레이어에서 에러처리를 전담하지않기 위함이다.(상위로 전파)
     */
    public Optional<UserDto> searchUser(String username) {
        return userRepository.findByUsername(username)
                .map(UserDto::fromEntity);
    }


    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 모든 유저를 조회한다.
     */
    public List<UserDto> selectUserList(UserDto userDto) {
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList()
                );
    }


//    /**
//     * User 클래스가 username 속성만 가지고 있다고 가정하고 작성한 것이다. 실제 User 클래스에 어떤 속성들이 있는지에 따라 이 코드를 적절히 수정해야 한다.
//     * 또한, 이 코드는 UserDetails 객체에서 사용자의 정보를 가져와 User 객체를 새로 생성하고 있다.
//     * 만약 UserDetails 객체가 이미 User 객체를 나타내고 있다면, 즉 UserDetails 객체가 User 클래스의 인스턴스라면, UserDetails 객체를 직접 User 타입으로 캐스팅하여 반환할 수 있다.
//     */
//    public SecurityUserDetailsDto getAuthenticatedUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null) {
//            throw new SecurityException("No authentication data provided");
//        }
//
//        Object principal = authentication.getPrincipal();
//        if (!(principal instanceof SecurityUserDetailsDto)) {
//            throw new SecurityException("Principal is not an instance of SecurityUserDetailsDto");
//        }
//
//        // Cast principal to SecurityUserDetailsDto
//        return (SecurityUserDetailsDto) principal;
//    }

    public JwtToken login(String loginId, String password) {
        // Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 검증된 인증정보로 JWT토큰 생성
        JwtToken token = jwtTokenProvider.generateToken(authentication);

        return token;
    }




}
