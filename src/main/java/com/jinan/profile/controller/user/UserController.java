package com.jinan.profile.controller.user;

import com.jinan.profile.domain.user.User;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * @PreAuthorize 어노테이션은 보안 표현식을 사용하여 더 복잡한 접근 제어를 수행할 수 있다.
 * 예를 들어, 특정 권한을 가진 사용자만 접근할 수 있도록 하거나, 메소드 인자를 기반으로 접근을 제어하는 것이 가능하다.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * JWT 토큰을 검증하는 로직은 JwtAuthorizationFilter에서 이미 구현했기 때문에, 별도로 JWT 토큰 검증 서비스를 만들 필요는 없다.
     * JwtAuthorizationFilter에서 JWT 토큰을 검증하고, 검증된 토큰에서 사용자 정보를 추출하여 SecurityContext에 저장하면, 이후에는 SecurityContext를 통해 인증된 사용자 정보를 얻을 수 있다.
     * 이렇게 하면, 각 컨트롤러 메소드에서는 별도로 JWT 토큰을 검증할 필요 없이, SecurityContext를 통해 인증된 사용자 정보를 사용할 수 있다.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public UserDto getProfile() {
        UserDetails userDetails = userService.getAuthenticatedUser();
        String username = userDetails.getUsername();

        // Use the username to fetch the user's profile from the database
        return Optional.ofNullable(userService.findByUsername(username))
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));
    }


}