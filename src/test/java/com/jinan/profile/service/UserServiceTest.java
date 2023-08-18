package com.jinan.profile.service;

import com.jinan.profile.config.TotalTestSupport;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest extends TotalTestSupport {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;


    @DisplayName("유저의 login 아이디를 통해서 유저정보를 조회한다.")
    @Test
    void findByLoginId() {
        //given
        User user = createUser();
        User savedUser = userRepository.save(user);

        //when
        UserDto actual = userService.findByLoginId(savedUser.getLoginId());

        //then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(UserDto.fromEntity(savedUser));
        assertThat(actual).isInstanceOf(UserDto.class);
        assertThat(actual).hasFieldOrPropertyWithValue("loginId", "dig04058");

    }

    private User createUser() {
        return User.of(
                "dig04058",
                "wlsdks12",
                "dig04058",
                "wlsdks12@naver.com",
                RoleType.ADMIN,
                UserStatus.Y
        );
    }

}