package com.jinan.profile.config;

import com.jinan.profile.config.security.SecurityConfig;
import com.jinan.profile.domain.type.RoleType;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.service.UserService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * 테스트에 필요한 security관련 설정을 여기서 전부 처리하기위해 만들었다.
 */
@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean
    private UserService userService;

    @BeforeTestMethod
    public void securitySetup() {
        given(userService.searchUser(anyString()))
                .willReturn(Optional.of(createUsersDto()));
    }

    // 테스트용 UsersDto를 만들어 준다.
    private UserDto createUsersDto() {
        return UserDto.of(
                10L,
                "wlsdks12",
                "wlsdks123",
                "{noop}pw",
                "wlsdks123@naver.com",
                RoleType.USER
        );
    }
}
