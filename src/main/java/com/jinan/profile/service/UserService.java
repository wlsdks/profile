package com.jinan.profile.service;

import com.jinan.profile.dto.security.SecurityUserDetailsDto;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserDto findByLoginId(String username) {
        return userRepository.findByLoginId(username)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * User 클래스가 username 속성만 가지고 있다고 가정하고 작성한 것이다. 실제 User 클래스에 어떤 속성들이 있는지에 따라 이 코드를 적절히 수정해야 한다.
     * 또한, 이 코드는 UserDetails 객체에서 사용자의 정보를 가져와 User 객체를 새로 생성하고 있다.
     * 만약 UserDetails 객체가 이미 User 객체를 나타내고 있다면, 즉 UserDetails 객체가 User 클래스의 인스턴스라면, UserDetails 객체를 직접 User 타입으로 캐스팅하여 반환할 수 있다.
     */
    public SecurityUserDetailsDto getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new SecurityException("No authentication data provided");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof SecurityUserDetailsDto)) {
            throw new SecurityException("Principal is not an instance of SecurityUserDetailsDto");
        }

        // Cast principal to SecurityUserDetailsDto
        return (SecurityUserDetailsDto) principal;
    }


}
