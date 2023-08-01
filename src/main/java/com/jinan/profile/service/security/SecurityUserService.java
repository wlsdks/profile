package com.jinan.profile.service.security;

import com.jinan.profile.dto.user.UserDto;

import java.util.Optional;

public interface SecurityUserService {
    Optional<UserDto> login(UserDto userDto);
}
